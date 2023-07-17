buildscript {
    val kotlinVersion by rootProject.extra { "1.8.10" }

    repositories {
        mavenCentral()
        google()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0-alpha04")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.mikepenz.aboutlibraries.plugin:aboutlibraries-plugin:10.6.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")


        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven("https://jitpack.io")
        // Required for Xposed API library
        jcenter()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
