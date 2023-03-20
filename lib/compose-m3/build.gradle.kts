@file:OptIn(ExperimentalComposeLibrary::class)
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


plugins {
    kotlin("multiplatform")
//    kotlin("android") apply false
//    id("com.android.application") apply false
//    id("com.android.library") apply false
    id("org.jetbrains.compose")
}
group = "me.heizi.compose.ext.monet.compose.kdrag0n.m3"
version = "1.0-SNAPSHOT"
kotlin {
    // android()
    jvmToolchain(19)

    val jvmTargetSystem = Attribute.of("system.platform", String::class.java)
    val fork = Attribute.of("system.platform.fork", String::class.java)
    jvm("windows") {
        attributes {
            attribute(jvmTargetSystem, "windows")
        }
    }
    jvm("windows-jna") {
        attributes {
            attribute(jvmTargetSystem, "windows")
            attribute(fork,"jna")
        }
    }
//    jvm("desktop") {
//        jvmToolchain(19)
//    }
//    jvm("jna") {
//        jvmToolchain(19)
//    }
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
        this["windowsMain"]?.windowsDependencies()
        this["windows-jnaMain"]?.apply {
            dependencies {
                rootProject.libs.run {
                    api(net.java.dev.jna.jna.platform)
                    api(net.java.dev.jna.jna.asProvider())
                }

            }
        }?.windowsDependencies()

        commonTest {
            dependencies {
                api(compose.uiTestJUnit4)
                compileOnly(compose.desktop.currentOs)
            }
        }
    }
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