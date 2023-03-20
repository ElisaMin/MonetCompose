import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    // add multiple compose plugin
    id("org.jetbrains.compose") version libs.versions.compose apply false
    kotlin("multiplatform") version libs.versions.kotlin.lang
    alias(libs.plugins.nl.littlerobots.version.catalog.update)
    alias(libs.plugins.com.github.ben.manes.versions)
}

group = "me.heizi.monet"
version = "1.0-SNAPSHOT"

//define repositories for all projects
allprojects {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}


// add context-receivers to kotlin compiler
tasks.withType(KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "19"
            }
        }
    }
    // jvm ()
    sourceSets {
        commonMain {

            dependencies {
                implementation("dev.kdrag0n:colorkt:${libs.versions.kotlin.color.get()}")
                implementation("io.github.aakira:napier:${libs.versions.kotlin.napier.get()}")
            }
        }
        commonTest {
//            dependencies {
//                testImplementation(platform("org.junit:junit-bom:5.9.1"))
//                testImplementation("org.junit.jupiter:junit-jupiter")
//            }
        }
    }
    // define jvm toolchain
    

}

dependencies {
}
