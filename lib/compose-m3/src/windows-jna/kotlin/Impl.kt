package me.heizi.compose.ext.monet.common

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinReg
import dev.kdrag0n.colorkt.rgb.Srgb
import dev.kdrag0n.monet.theme.Monet
interface DwmApi : Library {
    companion object {
        val instance: DwmApi = Native.load("dwmapi", DwmApi::class.java)
    }
    fun DwmGetColorizationColor(pcrColorization: WinDef.DWORDByReference, pfOpaqueBlend: WinDef.BOOLByReference): WinNT.HRESULT
}
fun DwmApi.systemSeekColor(): Int? = runCatching {
    WinDef.DWORDByReference().also {
        DwmGetColorizationColor(it, WinDef.BOOLByReference(WinDef.BOOL(false)))
    }.value.toInt()
}.getOrNull()
@Deprecated("fix up", ReplaceWith("systemSeekColor()"), DeprecationLevel.ERROR)
fun systemSeekColorReg(): Int? = runCatching {
    Advapi32Util.registryGetIntValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\DWM", "AccentColor").also {
        println(it)
//        Integer.valueOf(it.substring(8, 10), 16) shl 24 or
//        Integer.valueOf(it.substring(6, 8), 16) shl 16 or
//        Integer.valueOf(it.substring(4, 6), 16) shl 8 or
//        Integer.valueOf(it.substring(2, 4), 16)
    }
}.getOrNull()
actual fun systemIsDarkTheme(): Boolean = runCatching {
    Advapi32Util.registryGetIntValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\DWM", "AppsUseLightTheme")
}.getOrElse { runCatching {
    Advapi32Util.registryGetIntValue(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize", "AppsUseLightTheme")
}.getOrDefault(1) } == 0
actual fun systemSeekColor(): Srgb?
    = DwmApi.instance.systemSeekColor()?.let(::Srgb)

actual fun monetConfig(): Monet.Config
    = Monet.Config.Default