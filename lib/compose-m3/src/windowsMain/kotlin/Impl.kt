@file:JvmName("Impl")

package me.heizi.compose.ext.monet.common

import androidx.compose.ui.graphics.Color
import dev.kdrag0n.monet.theme.Monet


private val isWindowsSystem by lazy {
    System.getProperty("os.name").lowercase().startsWith("win")
}

actual val kdrag0nProvider: Kdrag0nProvider = object : Kdrag0nProvider {

    private fun regGet(path:String,key:String) = runCatching {
        ProcessBuilder("cmd", "/c", "reg", "query", path.replace("/","\\"),"/v",key).start().let { process ->
            @Suppress("ReplaceCallWithBinaryOperator")
            process.waitFor().equals(0) to
                    process.inputStream.use { it.bufferedReader(charset("gbk"))
                        .readText()
                        .splitToSequence(" ", "\n")
                        .filter { s -> s.isNotBlank() }
                        .last()
                    } .also { process.destroy() }
        }
    }.getOrDefault(false to "")

    private val DWM = "HKEY_CURRENT_USER/SOFTWARE/Microsoft/Windows/DWM"

    override fun isSystemDarkTheme(): Boolean {
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


    override fun systemColor(): Color? =
        runCatching {
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
    override val config: Monet.Config = Monet.Config.Default
}

