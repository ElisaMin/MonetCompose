@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform") version libs.versions.kotlin.lang
}

group = "me.heizi.monet"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
kotlin {
    jvm ()
    sourceSets {
        commonMain {
            dependencies {
                implementation("dev.kdrag0n:colorkt:${libs.versions.kotlin.color.get()}")
            }
        }
        commonTest {
//            dependencies {
//                testImplementation(platform("org.junit:junit-bom:5.9.1"))
//                testImplementation("org.junit.jupiter:junit-jupiter")
//            }
        }
    }
}

dependencies {

}
