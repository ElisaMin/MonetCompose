package me.heizi.compose.ext.monet.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.window.WindowScope
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

@Composable
@Suppress("unused", "FunctionName")
inline fun WindowScope.Monet(crossinline block: @Composable MonetWindow.() -> Unit) {
    MonetWindow.with {
        block()
    }
}

@get:Composable
actual val kdrag0nProvider: Kdrag0nProvider
    get() = MonetWindow.local.current
@Suppress("unused","MemberVisibilityCanBePrivate")
class MonetWindow private constructor(window: Window = Frame()):Kdrag0nProvider {

    companion object {
        val Default by lazy {
            MonetWindow()
        }
        val local = staticCompositionLocalOf {
            Default
        }
        context(WindowScope)
        @Composable
        fun with(block: @Composable MonetWindow.() -> Unit) {
            val monet by remember { mutableStateOf(MonetWindow()) }
            CompositionLocalProvider(local provides monet) {
                monet.block()
            }
        }
    }
    var color by mutableStateOf(Color(0x01579B))
    init {
        updateColorBySystemAccent()
    }
    //    @Composable
    override fun systemColor(): Color = color

    private val _windowStyler = WindowsWindowStyleManager(
        window = window,
    )
    override var config = Monet.Config.Default
        private set
    private var theScheme: ColorScheme.Material? by mutableStateOf(null)

    override fun getScheme(
        color: Color, dark: Boolean, config: Monet.Config
    ): ColorScheme.Material {
        return super.getScheme(color, dark, config).also { theScheme = it }
    }
    val windowStyler get() = _windowStyler.apply {
        (theScheme?:getScheme()).getWindowFrame().let { this.frameStyle = it }
    }
    override fun isSystemDarkTheme(): Boolean
            = windowStyler.isDarkTheme
    fun updateColorBySystemAccent() {
        DwmApi.instance.systemSeekColor()?.let { color = Color(it) }
    }
    fun config(config: (Monet.Config)-> Monet.Config) = apply {
        this.config = config(this.config)
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
