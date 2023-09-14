@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.org.jetbrains.compose) apply false
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.nl.littlerobots.version.catalog.update)
    alias(libs.plugins.com.github.ben.manes.versions)
    `maven-publish`
}

allprojects {

    apply(plugin = "maven-publish")
    group = "me.heizi.monet-kdrag0n"
    version = rootProject.libs.versions.kdrag0n.monet.get()

    // not working
//    tasks.withType<KotlinCompile> {
//        compilerOptions.languageVersion = KotlinVersion.KOTLIN_2_1
//    }

    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    publishing {
        repositories {
            maven {
                name = "test"
                url = uri("file://${rootProject.projectDir}/build/maven-repo/")
            }
            maven {
                name = "github"
                url = uri("https://maven.pkg.github.com/ElisaMin/MonetCompose")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }

        publications {
            val pom = { it:MavenPublication -> with(it) {
                pom {
                    name.set("Kdrag0nMonetCompose")
                    description.set("A Compose [windows only now] library for generating color palettes based on Kdrag0n's Monet Engine")
                    url.set("https://github.com/ElisaMin/MonetCompose")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://github.com/ElisaMin/MonetCompose/blob/main/LICENSE")
                        }
                        license {
                            name.set("MIT License - Heizi")
                            url.set("https://github.com/ElisaMin/MonetCompose/blob/main/lib/LICENSE")
                        }
                    }
                    developers {
                        developer {
                            id.set("ElisaMin")
                            name.set("ElisaMin")
                            email.set("Heizi@lge.fun")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/ElisaMin/MonetCompose.git")
                        developerConnection.set("scm:git:ssh://github.com/ElisaMin/MonetCompose.git")
                        url.set("https://github.com/ElisaMin/MonetCompose")
                    }
                }
            } }
            filterIsInstance<MavenPublication>().forEach(pom)
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