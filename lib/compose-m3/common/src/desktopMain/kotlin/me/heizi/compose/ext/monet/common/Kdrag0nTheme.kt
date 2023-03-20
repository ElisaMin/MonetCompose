@file:JvmName("Impl")
@file:Suppress("NOTHING_TO_INLINE")

package me.heizi.compose.ext.monet.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import dev.kdrag0n.colorkt.rgb.Srgb
import dev.kdrag0n.monet.theme.Monet


@Composable
private inline fun ColorBlock(color: Color,text:String="color") {
    Column {
        SelectionContainer {
            Text(text)
        }
        Box(
            Modifier
                .size(64.dp)
                .background(color)
                .shadow(1.dp)
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
internal fun main() {
    // simple windows compose app to shows the button and some other widgets to see how it works on color
    singleWindowApplication {
        Surface(Modifier.fillMaxSize()) {
            Column {
                Kdrag0nTheme {
                    Surface(Modifier.fillMaxSize()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(Modifier.padding(16.dp))
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
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        ColorBlock(windowsColor!!, "windowsColor")
                    }
                    item {
                        ColorBlock(systemSeekColor()!!.toComposeColor(),"${systemSeekColor()!!.toComposeColor().toArgb().let(::Srgb).toHex()} Seek Color")
                    }
                }
            }
        }
    }
}


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