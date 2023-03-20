@file:JvmName("Impl")
package me.heizi.compose.ext.monet.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.window.singleWindowApplication
import dev.kdrag0n.colorkt.rgb.Srgb
import dev.kdrag0n.monet.theme.Monet


@OptIn(ExperimentalMaterial3Api::class)
internal fun main() {
    // simple windows compose app to shows the button and some other widgets to see how it works on color
    singleWindowApplication {
        Kdrag0nTheme {
            Surface {
                Column {
                    LinearProgressIndicator()
                    var isClicked by remember { mutableStateOf(false) }
                    var text by remember { mutableStateOf("Text") }
                    Row {
                        Button(onClick = {isClicked = !isClicked}) { Text("Button") }
                        if (isClicked) Text(text)
                        RadioButton(isClicked, onClick = {isClicked = !isClicked})
                    }
                    Card {
                        TextField(text, onValueChange = {text = it})
                    }

                }
            }
        }
    }



}


private val isWindowsSystem by lazy {
    System.getProperty("os.name").lowercase().startsWith("win")
}

@Suppress("NOTHING_TO_INLINE")
private inline fun regGet(path:String,key:String) =
    ProcessBuilder("cmd", "/c", "reg", "query", path.replace("/","\\"),"/v",key).start().let { process ->
        process.exitValue().equals(0) to
        process.inputStream.use { it.bufferedReader(charset("gbk"))
            .readText()
            .splitToSequence(" ", "\n",)
            .filter { it.isNotBlank() }
            .last()
        } .also { process.destroy() }
    }
@Composable
actual fun systemIsDarkTheme(): Boolean {
    // todo macos dark theme or linux
    if (!isWindowsSystem) return false
    // find windows 11 first , find windows 10 if its failed or not found
    // windows 11 HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Themes\Personalize\AppsUseLightTheme
    // windows 10 HKEY_CURRENT_USER\SOFTWARE\Microsoft\Windows\DWM\AppsUseLightTheme
    fun getValue(pair: Pair<Boolean,String>):Int? = pair.let { (isSuccess, value) ->
        if (!isSuccess) return null
        value.replace("0x","").toIntOrNull()
    }
    return (getValue(regGet("HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize","AppsUseLightTheme"))
        ?:  getValue(regGet(DWM,"AppsUseLightTheme"))
        ?: 1 ) == 0
}
private const val DWM = "HKEY_CURRENT_USER/SOFTWARE/Microsoft/Windows/DWM"
actual fun systemSeekColor(): Srgb? {
    if (isWindowsSystem) runCatching {
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
        ).let {
            Srgb(it.toArgb())
        }
    }


    return null
}
actual fun monetConfig(): Monet.Config
    = Monet.Config.default