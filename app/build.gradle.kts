plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.parcelize)
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

        testInstrumentationRunner = "com.mironov.coursework.CustomRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    testOptions {
        animationsDisabled = true
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
    implementation(libs.coil)
    implementation(libs.loggin.interceptor)

    implementation(libs.elmslie.core)
    implementation(libs.elmslie.android)

    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(libs.kaspresso)
    implementation(libs.androidx.rules)

    debugImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.httpclient.android)
    androidTestImplementation(libs.wiremock) {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }

    androidTestImplementation(libs.hamcrest)
    debugImplementation (libs.fragment.testing)

    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
}