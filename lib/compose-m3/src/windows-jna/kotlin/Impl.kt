@file:Suppress("FunctionName", "FunctionName")

package me.heizi.compose.ext.monet.common

import androidx.compose.ui.graphics.Color
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinReg
import dev.kdrag0n.monet.theme.Monet

private interface DwmApi : Library {
    companion object {
        val instance: DwmApi = Native.load("dwmapi", DwmApi::class.java)
    }
    fun DwmGetColorizationColor(pcrColorization: WinDef.DWORDByReference, pfOpaqueBlend: WinDef.BOOLByReference): WinNT.HRESULT
}
@Suppress("NOTHING_TO_INLINE")
private inline fun DwmApi.systemSeekColor(): Int? = runCatching {
    WinDef.DWORDByReference().also {
        DwmGetColorizationColor(it, WinDef.BOOLByReference(WinDef.BOOL(false)))
    }.value.toInt()
}.getOrNull()

actual val kdrag0nProvider: Kdrag0nProvider get() = object : Kdrag0nProvider {
    override fun isSystemDarkTheme(): Boolean = runCatching {
        Advapi32Util.registryGetIntValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\DWM", "AppsUseLightTheme")
    }.getOrElse { runCatching {
        Advapi32Util.registryGetIntValue(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize", "AppsUseLightTheme")
    }.getOrDefault(1) } == 0

    override fun systemColor(): Color? = DwmApi.instance.systemSeekColor()?.let(::Color)
    override val config: Monet.Config = Monet.Config.Default
}