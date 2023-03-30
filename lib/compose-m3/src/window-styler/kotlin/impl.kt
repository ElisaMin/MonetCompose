package me.heizi.compose.ext.monet.common

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.WindowScope
import com.mayakapps.compose.windowstyler.WindowBackdrop
import com.mayakapps.compose.windowstyler.WindowCornerPreference
import com.mayakapps.compose.windowstyler.WindowFrameStyle
import com.mayakapps.compose.windowstyler.windows.WindowsWindowStyleManager
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinReg
import dev.kdrag0n.monet.theme.ColorScheme
import dev.kdrag0n.monet.theme.Monet
import me.heizi.compose.ext.monet.common.DwmApi.Companion.systemSeekColor
import java.awt.Window

// function expect list:
//  - default window styler manager
//  - window frame covert from color scheme
//  - jna calling system color in windows and more or something

/**
 * Composable function that creates a Monet window.
 * It provides a MonetWindow instance to its content via CompositionLocalProvider.
 * @param block the composable block to be executed.
 */
@Composable
@Suppress("unused", "FunctionName")
inline fun WindowScope.Monet(crossinline block: @Composable MonetWindow.() -> Unit) {
    MonetWindow.with {
        block()
    }
}

@Suppress("unused","MemberVisibilityCanBePrivate")
class MonetWindow private constructor(window: Window):Kdrag0nProvider {

    companion object {
        val systemColor by lazy {
            runCatching {
                DwmApi.instance.systemSeekColor()
            }.getOrNull()
        }
        val isDark by lazy {
            runCatching {
                Advapi32Util.registryGetIntValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\DWM", "AppsUseLightTheme")
            }.getOrElse { runCatching {
                Advapi32Util.registryGetIntValue(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize", "AppsUseLightTheme")
            }.getOrDefault(1) } == 0
        }
        val local: ProvidableCompositionLocal<MonetWindow> = staticCompositionLocalOf {
            throw NotImplementedError("MonetWindow is not provided")
        }
        /**
         * Composable function that creates a MonetWindow instance and provides it to its content via CompositionLocalProvider.
         *
         * @param block the composable block to be executed.
         */
        context(WindowScope)
        @Composable
        fun with(block: @Composable MonetWindow.() -> Unit) {
            val monet by remember { mutableStateOf(MonetWindow(window)) }
            CompositionLocalProvider(local provides monet) {
                // fixme
                Kdrag0nTheme(seekColor = monet.color) {
                    monet.block()
                }
            }
        }
    }
    var color by mutableStateOf(Color(0x01579B))
//    @Composable
    // fixme update failed
    override fun systemColor(): Color = color
    override var config = Monet.Config.Default
        private set
    private var theScheme: ColorScheme.Material? by mutableStateOf(null)
    private val _windowStyler = WindowsWindowStyleManager(
        window = window,
    )
    override fun getScheme(
        color: Color, dark: Boolean, config: Monet.Config
    ): ColorScheme.Material {
        return super.getScheme(color, dark, config).also { theScheme = it }
    }
    val windowStyler get() = _windowStyler.apply {
        (theScheme?:getScheme()).getWindowFrame().let { this.frameStyle = it }
    }

    override fun isSystemDarkTheme(): Boolean
        = isDark
    init {
        updateColorBySystemAccent()
    }
    fun updateColorBySystemAccent(
        backdrop: WindowBackdrop = WindowBackdrop.Default,
    ) {
        if (color == Color(0x01579B))
            color =  Color(systemColor?:0x01579B)
        getScheme()
        windowStyler.isDarkTheme = isDark
        windowStyler.backdropType = backdrop
        systemColor
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
actual val kdrag0nProvider: Kdrag0nProvider
    @Composable
    get() {
        return MonetWindow.local.current
    }
