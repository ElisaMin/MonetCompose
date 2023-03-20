@file:JvmName("Impl")
@file:Suppress("NOTHING_TO_INLINE")

package me.heizi.compose.ext.monet.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dev.kdrag0n.colorkt.rgb.Srgb
import dev.kdrag0n.monet.theme.Monet


private val isWindowsSystem by lazy {
    System.getProperty("os.name").lowercase().startsWith("win")
}

private inline fun regGet(path:String,key:String) = runCatching {
    ProcessBuilder("cmd", "/c", "reg", "query", path.replace("/","\\"),"/v",key).start().let { process ->
        process.waitFor().equals(0) to
        process.inputStream.use { it.bufferedReader(charset("gbk"))
            .readText()
            .splitToSequence(" ", "\n",)
            .filter { it.isNotBlank() }
            .last()
        } .also { process.destroy() }
    }
}.getOrDefault(false to "")


//fun
@Composable
actual fun systemIsDarkTheme(): Boolean {
    // todo macos dark theme or linux
    if (!isWindowsSystem) return false
    // find windows 11 first , find windows 10 if its failed or not found
    // windows 11 HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Themes\Personalize\AppsUseLightTheme
    // windows 10 HKEY_CURRENT_USER\SOFTWARE\Microsoft\Windows\DWM\AppsUseLightTheme
    fun getValue(pair: Pair<Boolean,String>):Int? = pair.let { (isSuccess, value) ->
        if (!isSuccess) return null
        value.removePrefix("0x").trim().toIntOrNull().also(::println)
    }
    return (getValue(regGet("HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize","AppsUseLightTheme"))
        ?:  getValue(regGet(DWM,"AppsUseLightTheme"))
        ?: 1 ) == 0
}
private const val DWM = "HKEY_CURRENT_USER/SOFTWARE/Microsoft/Windows/DWM"
private val windowsColor: Color? get() = runCatching {
    val (isSuccess,color) = regGet(DWM, "AccentColor")
    require(isSuccess) {
        "cant get color from dwm $DWM,AccentColor"
    }
    color
}.getOrNull()?.let { s ->
    Color(
        Integer.valueOf(s.substring(8, 10), 16),
        Integer.valueOf(s.substring(6, 8), 16),
        Integer.valueOf(s.substring(4, 6), 16),
        Integer.valueOf(s.substring(2, 4), 16),
    )
}

actual fun systemSeekColor(): Srgb? {
    if (isWindowsSystem) return windowsColor?.let {
        Srgb(it.toArgb())
    }
    return null
}
actual fun monetConfig(): Monet.Config
    = Monet.Config.default