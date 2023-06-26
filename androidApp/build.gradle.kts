import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id( "kotlin-parcelize")
    id("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

apply(from = "../ktlint.gradle")

android {
    namespace = "dev.luteoos.scrumbet.android"
    compileSdk = 33

    lint {
        abortOnError = false
        quiet = true
    }

    defaultConfig {
        applicationId = "dev.luteoos.scrumbet"
        minSdk = 28
        targetSdk = 33
        versionCode = 5
        versionName = "0.1.4"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
//            the<CrashlyticsExtension>().nativeSymbolUploadEnabled = true bugged
        }
    }

    flavorDimensionList.add("env")

    productFlavors {
        create("localhost"){
            dimension = "env"
            applicationIdSuffix = ".localhost"
        }
        create("azure"){
            dimension = "env"
            applicationIdSuffix = ".azure"
        }
    }

    kotlinOptions{
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.1.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ndk")

    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(Dependencies.kotlinCoroutinesAndroid)
    implementation(Dependencies.kotlinAndroidCoroutinesRuntime)
    implementation(Dependencies.kotlinReflect)
    implementation(Dependencies.koinCore)
    implementation(Dependencies.koinAndroid)
    implementation(Dependencies.timber)
    implementation("com.github.Luteoos:qrx:1.0.1")
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")

    //QRCode gen
    implementation("com.google.zxing:core:3.3.0")

    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.material:material:1.4.3")
//    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.4.3")
    implementation("androidx.compose.ui:ui-tooling:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.compose.foundation:foundation:1.4.3")
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.activity:activity-ktx:1.7.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation(Dependencies.materialThemeAdapter)
}