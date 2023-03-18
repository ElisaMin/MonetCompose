plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.mikepenz.aboutlibraries.plugin")
}

android {
    compileSdk = 31
    buildToolsVersion = "31.0.0"

    defaultConfig {
        applicationId = "dev.kdrag0n.android12ext"
        minSdk = 30
        targetSdk = 31
        versionCode = 9000000
        versionName = "9.0.0-test1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packagingOptions {
        // Module info for kotlin-reflect - leaks info
        exclude("/META-INF/*.kotlin_module")
        exclude("/META-INF/*.version")
        // Builtin info for kotlin-reflect
        exclude("/kotlin/**")
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    compileOnly("de.robv.android.xposed:api:82")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.fragment:fragment:1.3.6")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    implementation("androidx.core:core-splashscreen:1.1.0-alpha01")
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-compiler:2.45")

    implementation("de.maxr1998:modernandroidpreferences:2.3.2")
    implementation("com.microsoft.design:fluent-system-icons:1.1.196")
    implementation("dev.chrisbanes.insetter:insetter:0.6.1")
    implementation("com.crossbowffs.remotepreferences:remotepreferences:0.8")
    implementation("com.mikepenz:aboutlibraries:10.6.1")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
    implementation("com.jaredrummler:colorpicker:1.1.0")
    implementation("com.github.topjohnwu.libsu:core:3.1.2")
    implementation("com.github.Zhuinden:fragmentviewbindingdelegate-kt:1.0.2")
    implementation("com.davemorrissey.labs:subsampling-scale-image-view-androidx:3.10.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi:1.14.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    implementation("dev.kdrag0n:colorkt:1.0.5")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
}
