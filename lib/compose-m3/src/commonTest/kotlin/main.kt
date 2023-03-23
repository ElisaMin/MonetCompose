@file:Suppress("NOTHING_TO_INLINE", "TestFunctionName")
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
import androidx.compose.ui.unit.dp
import me.heizi.compose.ext.monet.common.Kdrag0nTheme
import me.heizi.compose.ext.monet.common.systemSeekColor
import me.heizi.compose.ext.monet.common.toComposeColor


@Suppress("SameParameterValue")
@Composable
private inline fun ColorBlock(color: Color, text:String="color") {
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
@Composable
fun TestScreen() {
    // simple windows compose app to shows the button and some other widgets to see how it works on color
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
            LazyRow(
                Modifier.requiredHeight(70.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
//                    item {
//                        ColorBlock(!!, "windowsColor")
//                    }
                item {
                    ColorBlock(
                        systemSeekColor()!!.toComposeColor(),"Seek Color")
                }
            }
        }
    }
}