@file:OptIn(ExperimentalComposeLibrary::class)
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
//    kotlin("android") apply false
//    id("com.android.application") apply false
//    id("com.android.library") apply false
}

val jvmTargetSystem = Attribute.of("system.platform", String::class.java)
val fork = Attribute.of("system.platform.fork", String::class.java)
infix fun AttributeContainer.windowsFork(forkName:String) {
    attribute(jvmTargetSystem, "windows")
    attribute(fork, forkName)
}
infix fun AttributeContainer.platformFork(platformAndFork : Pair<String,String>) {
    val (platform,forks) = platformAndFork
    attribute(jvmTargetSystem, platform)
    attribute(fork, forks)
}
kotlin {
    // android()
    jvmToolchain(19)
    jvm("windows") target@{
        attributes windowsFork "main"
    }
    jvm("windows-jna") target@ {
        attributes windowsFork "jna"
        // set main sources set the default dir windows-jnaMain to windows-jna
        compilations["main"].defaultSourceSet {
            kotlin.srcDir("src/windows-jna/kotlin")
        }
    }
    jvm("window-styler") target@ {
        attributes platformFork ("multiplatform" to "window-styler")
        compilations["main"].defaultSourceSet {
            kotlin.srcDir("src/window-styler/kotlin")
        }
        compilations.all {
            // enable kotlin preview feature : context receiver :
            kotlinOptions {
                freeCompilerArgs+= "-Xcontext-receivers"
            }

        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation("dev.kdrag0n:colorkt:${rootProject.libs.versions.kotlin.color.get()}")
                implementation(rootProject)
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
            }
        }
        // windows
        fun KotlinSourceSet.windowsDependencies() {
            dependencies {
                api(compose.runtime)
                compileOnly(compose.desktop.windows_x64)
            }
        }
        findByName("windowsMain")?.windowsDependencies()
        findByName("windows-jnaMain")?.also {
            it.dependencies {
                rootProject.libs.run {
                    api(net.java.dev.jna.jna.platform)
                    api(net.java.dev.jna.jna.asProvider())
                }
            }
        }?.windowsDependencies()
        findByName("window-stylerMain")?.run {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                rootProject.libs {
                    api(com.mayakapps.compose.window.styler)
                    api(net.java.dev.jna.jna.platform)
                    api(net.java.dev.jna.jna.asProvider())
                }
            }
        }
        commonTest {
            dependencies {
                api(compose.uiTestJUnit4)
                compileOnly(compose.desktop.currentOs)
            }
        }
    }
}
configurations.forEach {
    with(it.name) {when {
        startsWith("windows-jna")
            -> it.attributes windowsFork "jna"
        startsWith("window") && !startsWith("windows")
            -> it.attributes platformFork ("multiplatform" to "window-styler")
        startsWith("windows")
            -> it.attributes windowsFork "main"
    } }
}
//android {
//    compileSdkVersion(33)
//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    defaultConfig {
//        minSdkVersion(24)
//        targetSdkVersion(33)
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//}