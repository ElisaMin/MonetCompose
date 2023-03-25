@file:OptIn(ExperimentalComposeLibrary::class)
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.internal.kapt.incremental.metadataDescriptor
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import kotlin.system.exitProcess


plugins {
    kotlin("multiplatform")
//    kotlin("android") apply false
//    id("com.android.application") apply false
//    id("com.android.library") apply false
    id("org.jetbrains.compose")
}






//configure<SourceSetContainer> {
//    getByName("windowsMain")
//}
val jvmTargetSystem = Attribute.of("system.platform", String::class.java)
val fork = Attribute.of("system.platform.fork", String::class.java)
infix fun AttributeContainer.windowsFork(suboption:String) {
    attribute(jvmTargetSystem, "windows")
    attribute(fork, suboption)
}
kotlin {
    // android()
    jvmToolchain(19)
    fun KotlinJvmTarget.publushToMaven() {
        val setName = this.name
        publishing {
            publications {
                getByName<MavenPublication>(setName) {
                    groupId = project.group.toString()
                    version = project.version.toString()
                    artifactId = project.name+"-"+setName

//                    artifact(tasks.findByName("javadocJar")!!)
//                    artifact(tasks.findByName("sourcesJar")!!)
                }
            }
        }
    }
    jvm("windows") target@{
        attributes windowsFork "main"
        publushToMaven()
    }
    jvm("windows-jna") target@ {
        attributes windowsFork "jna"
        publushToMaven()
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
        this["windowsMain"].windowsDependencies()
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
// fix Variants 'windowsRuntimeElements-published' and 'windows-jnaRuntimeElements-published' have the same attributes and capabilities.
configurations.forEach {
    with(it.name) {when {
        startsWith("windows-jna")
            -> it.attributes windowsFork "jna"
        startsWith("windows")
            -> it.attributes windowsFork "main"
    } }
}
configurations {

    map {
        it.name
    }.forEach(::println)
    //config windows-jna metadata api elements
    // errors :
    //  Variants 'windowsRuntimeElements-published' and 'windows-jnaRuntimeElements-published' have the same attributes and capabilities.
    //  Variants 'windowsRuntimeElements-published' and 'windows-jnaRuntimeElements-published' have the same attributes and capabilities. Please make sure either attributes or capabilities are different.
    // fix
//    findByName("windowsRuntimeElements")?.attributes
//    println(findByName("windows-jnaRuntimeElements")?.attributes)

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