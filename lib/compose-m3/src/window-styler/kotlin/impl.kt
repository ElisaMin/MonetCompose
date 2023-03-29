package me.heizi.compose.ext.monet.common

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.mayakapps.compose.windowstyler.WindowCornerPreference
import com.mayakapps.compose.windowstyler.WindowFrameStyle
import com.mayakapps.compose.windowstyler.windows.WindowsWindowStyleManager
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import dev.kdrag0n.colorkt.rgb.Srgb
import dev.kdrag0n.monet.theme.ColorScheme
import dev.kdrag0n.monet.theme.Monet
import me.heizi.compose.ext.monet.common.DwmApi.Companion.systemSeekColor
import java.awt.Frame
import java.awt.Window

// function expect list:
//  - default window styler manager
//  - window frame covert from color scheme
//  - jna calling system color in windows and more or something

@Suppress("unused","MemberVisibilityCanBePrivate")
class MonetWindow private constructor(val window: Window = Frame()) {

    companion object {
        val Default by lazy {
            MonetWindow()
        }
        val local = staticCompositionLocalOf {
            Default
        }
        private inline val Window.self get() = this
        context (Window) fun Monet.of() = MonetWindow(self)
        context (Window)
        @Composable
        inline fun Monet.of(crossinline block:@Composable MonetWindow.()->Unit ) {
            block(Monet.of())
        }
    }
    var color:Srgb by mutableStateOf(Srgb(0x01579B))
        private set

    var current:ColorScheme.Material? = _current
        private set

    var config = Monet.Config.Default
        private set

    infix fun scheme(color:Color):MonetWindow {
        this.color = Srgb(color.toArgb())
        current = _current
        return this
    }

    private val windowStyler = WindowsWindowStyleManager(
        window = this.window,
    )

    private val _current get() = ColorScheme.Dynamic[color,config].run {
        if (windowStyler.isDarkTheme) materialDark() else materialLight()
    }

    fun clean() {
        current = null
    }
    fun toStyler(
        cornerPreference: WindowCornerPreference = WindowCornerPreference.DEFAULT,
    ) = windowStyler.apply {
        current?.getWindowFrame(cornerPreference)?.let { this.frameStyle = it }
        clean()
    }
    fun updateScheme() {
        current = _current
    }
    fun updateSystemColor() {
        clean()
        DwmApi.instance.systemSeekColor()?.let { color = Srgb(it) }
        scheme(Color(color.toRgb8()))
        updateScheme()
    }
    fun isDarkTheme() = windowStyler.isDarkTheme
    fun config(config: (Monet.Config)-> Monet.Config) = apply {
        this.config = config(this.config)
    }
    @Composable operator fun invoke(
        content: @Composable (MonetWindow)->Unit
    ) {
        CompositionLocalProvider(
            local provides this,
            LocalSeekColorProvider provides color,
        ) {
            remember(color,windowStyler) { this }
            content(this)
        }
    }
    init {
        updateSystemColor()
    }
}

fun ColorScheme.Material.getWindowFrame(
    cornerPreference: WindowCornerPreference = WindowCornerPreference.DEFAULT,
) = WindowFrameStyle(
    titleBarColor = surface.toComposeColor(),
    borderColor = surface.toComposeColor(),
    captionColor = onSurface.toComposeColor(),
    cornerPreference = cornerPreference,
)

@Composable
actual fun systemIsDarkTheme(): Boolean = MonetWindow.local.current.isDarkTheme()
actual fun systemSeekColor(): Srgb? = DwmApi.instance.systemSeekColor()?.let { Srgb(it) }
actual fun monetConfig(): Monet.Config = Monet.Config.Default


@Suppress("FunctionName")
private interface DwmApi : Library {
    companion object {
        val instance: DwmApi get() = Native.load("dwmapi", DwmApi::class.java)
        @Suppress("unused","NOTHING_TO_INLINE")
        inline fun DwmApi.systemSeekColor(): Int? = runCatching {
            WinDef.DWORDByReference().also {
                DwmGetColorizationColor(it, WinDef.BOOLByReference(WinDef.BOOL(false)))
            }.value.toInt()
        }.getOrNull()
    }
    fun DwmGetColorizationColor(pcrColorization: WinDef.DWORDByReference, pfOpaqueBlend: WinDef.BOOLByReference): WinNT.HRESULT
}
