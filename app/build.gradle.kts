plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinxSerialization)
}

android {
    namespace = "com.mironov.coursework"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mironov.coursework"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.lifecycle.vm)
    implementation(libs.lifecycle.rt)
    implementation(libs.fragment)

    implementation(libs.cicerone)

    implementation(libs.dagger.core)
    ksp(libs.dagger.compiler)

    implementation(libs.shimmer)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
}