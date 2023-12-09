plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version Versions.kotlin
    kotlin("native.cocoapods")
    id("com.android.library")
//    id("dev.icerock.moko.kswift") version Versions.mokoKSwift
}

apply(from = "../ktlint.gradle")

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosArm64()

//    kswift {
//        install(dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature){
//            filter = excludeFilter("ClassContext/:dev.luteoos.scrumbet.data.dto.DataNowResponseItem")
//        }
//    }

    cocoapods {
        summary = "Data module"
        homepage = ""
        ios.deploymentTarget = "14.1"
        osx.deploymentTarget = "13.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "data"
        }

        xcodeConfigurationToNativeBuildType["Production"]=
            org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
    }
    
    sourceSets {
        val commonMain by getting{
            dependencies {
                implementation(Dependencies.kotlinStdlib)
                implementation(Dependencies.kotlinSerializationJson)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting{
            dependsOn(commonMain)
        }
//        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val macosArm64Main by getting{
            dependsOn(iosMain)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
        targetSdk = 34
    }
}