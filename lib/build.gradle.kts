import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform") version libs.versions.kotlin.lang
}

group = "me.heizi.monet"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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
