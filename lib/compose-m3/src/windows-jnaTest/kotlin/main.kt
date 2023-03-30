import dev.kdrag0n.monet.theme.ColorScheme
import me.heizi.compose.ext.monet.common.kdrag0nProvider
import me.heizi.compose.ext.monet.common.lightM3Scheme
import me.heizi.compose.ext.monet.common.systemIsDarkTheme
import me.heizi.compose.ext.monet.common.systemSeekColor
import org.junit.Test


class IMPL {
    val provider = kdrag0nProvider
    private var timeContainer = 0L
    fun time(name:String?) = System.currentTimeMillis().also {
        if (name !=null) println("Time: ${it - timeContainer} ! $name")
        timeContainer = it
    }
    inline fun time(name: String = "",crossinline ignoredBlock:()->Unit) {
        println("==================$name======================")
        time(null)
        ignoredBlock()
        time("")
        println("==================$name======================")
        println()
    }
    @Test
    fun test() {
        println("Hello, world!")
        time(null)
    }
    @Test
    fun systemDarkMode() = time("dark") {
        println(systemIsDarkTheme())
    }
//    @Test
//    fun systemColorR() = time("reg") {
//        println(systemSeekColorReg()?.let(::Srgb)?.toHex())
//    }
    @Test
    fun systemColor() = time("color") {
        println(systemSeekColor()?.toHex())
    }
    @Test
    fun preload() = time("preload") {
        println(systemSeekColor()?.toHex())
        time("dark")
        println(systemIsDarkTheme())
    }
    @Test
    fun seekColorToScheme() = time("seekColorToScheme") {
        ColorScheme.Dynamic[systemSeekColor()!!].lightM3Scheme()
    }
}