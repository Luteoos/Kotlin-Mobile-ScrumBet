plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosArm64()

    cocoapods {
        summary = "Domain layer module for SceumBete"
        homepage = ""
        version = "1.0"
        ios.deploymentTarget = "14.1"
        osx.deploymentTarget = "13.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "domain"
        }

        xcodeConfigurationToNativeBuildType["Production"]=
            org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":data"))
                implementation(Dependencies.kotlinStdlib)
                implementation(Dependencies.kotlinCoroutinesCore)
                implementation(Dependencies.kotlinSerializationJson)
                implementation(Dependencies.ktor)
                implementation(Dependencies.ktorSerialization)
                implementation(Dependencies.ktorAuth)
                implementation(Dependencies.ktorLogging)
                implementation(Dependencies.ktorContentNegotiation)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting{
            dependsOn(commonMain)
            dependencies {
                implementation(Dependencies.kotlinCoroutinesAndroid)
                implementation(Dependencies.ktorOkHttp)
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(Dependencies.ktorDarwin)
                implementation(Dependencies.ktorCio)
            }
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
    namespace = "dev.luteoos.scrumbet.domain"
    compileSdk = 34
    defaultConfig {
        minSdk = 26
    }
}