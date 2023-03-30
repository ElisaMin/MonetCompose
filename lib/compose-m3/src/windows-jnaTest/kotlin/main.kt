import dev.kdrag0n.monet.theme.ColorScheme
import me.heizi.compose.ext.monet.common.kdrag0nProvider
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
        println(kdrag0nProvider.isSystemDarkTheme())
    }
    @Test
    fun systemColor() = time("color") {
        println(kdrag0nProvider.systemColor())
    }
    @Test
    fun preload() = time("preload") {
        println(provider.systemColor())
        time("dark")
        println(provider.isSystemDarkTheme())
        time("loaded")
        println(provider.isSystemDarkTheme())
    }
    @Test
    fun seekColorToScheme() = time("seekColorToScheme") {
        val scheme = kdrag0nProvider.getScheme()
        time("scheme")
        scheme::class.java.declaredFields.forEach {
            it.isAccessible = true
            println("${it.name} = ${it.get(scheme)}")
        }
    }
}