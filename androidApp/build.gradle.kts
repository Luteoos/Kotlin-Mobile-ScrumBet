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
        versionCode = 10
        versionName = "0.4.0"
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

    flavorDimensionList.add("environment")

    productFlavors {
        create("localhost"){
            dimension = "environment"
            applicationIdSuffix = ".localhost"
        }
        create("azure"){
            dimension = "environment"
            applicationIdSuffix = ".azure"
        }
        create("production"){
            isDefault = true
            dimension = "environment"
            applicationIdSuffix = ""
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

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(Dependencies.kotlinCoroutinesAndroid)
    implementation(Dependencies.kotlinAndroidCoroutinesRuntime)
    implementation(Dependencies.kotlinReflect)
    implementation(Dependencies.koinCore)
    implementation(Dependencies.koinAndroid)
    implementation(Dependencies.timber)
    implementation("com.github.Luteoos:qrx:1.0.1")
    implementation(Dependencies.accompanistPermissions)

    //QRCode gen
    implementation("com.google.zxing:core:3.3.0")

    implementation(Dependencies.activityCompose)
    implementation(Dependencies.activityKtx)
    implementation(Dependencies.material)
    implementation(Dependencies.M3)
    implementation(Dependencies.M3WindowSize)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeRuntime)
    implementation(Dependencies.composeFoundtaion)
    implementation(Dependencies.composeTooling)
    implementation(Dependencies.composePreview)
    implementation(Dependencies.navigationUi)
    implementation(Dependencies.navigationFragment)
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation(Dependencies.accompanistMDC)
    implementation(Dependencies.appUpdate)
    implementation(Dependencies.appUpdateKtx)

    implementation("com.github.tehras:charts:0.2.4-alpha")
}