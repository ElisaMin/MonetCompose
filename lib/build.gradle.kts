import nl.littlerobots.vcu.plugin.versionCatalogUpdate
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.org.jetbrains.compose) apply false
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.nl.littlerobots.version.catalog.update)
    alias(libs.plugins.com.github.ben.manes.versions)
    `maven-publish`
}

allprojects {
    // apply maven publish
    apply(plugin = "maven-publish")
    group = "me.heizi.monet-kdrag0n"
    version = rootProject.libs.versions.kdrag0n.monet.get()

    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    publishing {

//        // config kotlin multiplatfrom maven publishig
//        publications {
//            create<MavenPublication>("maven") {
//                from(components["kotlin"])
//            }
//        }
        repositories {
            maven {
                name = "repo-build-test"
                url = uri("file://${rootProject.projectDir}/build/maven-repo/")
            }
        }
    }
}


kotlin {
    jvmToolchain(19)
    jvm ()
    sourceSets {
        commonMain {
            dependencies {
                implementation("dev.kdrag0n:colorkt:${libs.versions.kotlin.color.get()}")
                implementation("io.github.aakira:napier:${libs.versions.kotlin.napier.get()}")
            }
        }
    }
}