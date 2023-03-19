plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
//    id("com.android.library")
}

group = "me.heizi.compose.ext"
version = "1.0-SNAPSHOT"

kotlin {
    // android()
    jvm("desktop") {
        jvmToolchain(19)
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("dev.kdrag0n:colorkt:${rootProject.libs.versions.kotlin.color.get()}")
                implementation(rootProject)
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
            }
        }
//        val commonTest by getting {
//            dependencies {
//                implementation(kotlin("test"))
//            }
//        }
//        val androidMain by getting {
//            dependencies {
//                api("androidx.appcompat:appcompat:1.5.1")
//                api("androidx.core:core-ktx:1.9.0")
//            }
//        }
//        val androidTest by getting {
//            dependencies {
//                implementation("junit:junit:4.13.2")
//            }
//        }
//        val desktopMain by getting {
//            dependencies {
//                api(compose.preview)
//            }
//        }
//        val desktopTest by getting
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