import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}
//val m3Pj = project(":compose-m3").kotlinExtension.sourceSets.findByName("window-stylerMain")!!
//
//kotlin {
//    this.sourceSets["main"].run {
//        dependsOn(m3Pj)
//    }
//}
dependencies {
    implementation(compose.desktop.currentOs)
//    m3Pj.let {
//        logger.warn(it.name)
//        logger.warn(it.sourceSets.size.toString())
//        it.sourceSets.forEach {
//            logger.warn(it.name)
//        }
//        rootProject.artifacts
//    }
//
}