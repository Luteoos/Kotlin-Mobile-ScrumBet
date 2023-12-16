import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

import java.io.File
import java.io.FileInputStream
import java.util.Properties

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version Versions.kotlin
    kotlin("native.cocoapods")
    id("com.android.library")
    id("dev.icerock.moko.kswift") version "0.6.1"
    id("com.codingfeline.buildkonfig")
}

apply(from = "../ktlint.gradle")

version = "1.0"

buildkonfig {
    // gradlew generatebuildkonfig
    packageName = "dev.luteoos.scrumbet"


    val localProperties = Properties()
    localProperties.load(FileInputStream(rootProject.file("local.properties")))

    defaultConfigs{
        buildConfigField(STRING, "osAppId", localProperties["ONESIGNAL_APP_ID"].toString(), const = true)
        buildConfigField(STRING, "appVersion", "0.4", const = true)
        buildConfigField(STRING, "sslPrefix", "http://", const = true)
    }
}

kotlin {
    android {
        compilations.all{
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    iosX64(){
        binaries.executable {
//            if (System.getenv("XCODE_VERSION_MAJOR") == "1500") {
//            linkerOpts += "-ld64"
//            }
        }
    }
    iosArm64(){
        binaries.executable {
//            if (System.getenv("XCODE_VERSION_MAJOR") == "1500") {
//            linkerOpts += "-ld64"
//            }
        }
    }
    iosSimulatorArm64(){
        binaries.executable {
//            if (System.getenv("XCODE_VERSION_MAJOR") == "1500") {
//                linkerOpts += "-ld64"
//            }
        }
    }
    macosArm64(){
        binaries.executable {
//            if (System.getenv("XCODE_VERSION_MAJOR") == "1500") {
//            linkerOpts += "-ld64"
//            }
        }
    }

    cocoapods {
        summary = "Core module with State control, exports as api() project(:data)"
        homepage = "luteoos.dev"
        ios.deploymentTarget = "14.1"
        osx.deploymentTarget = "13.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "core"
            export(project(":data"))
            export(project(":domain"))
            transitiveExport = false
        }

        xcodeConfigurationToNativeBuildType["Production"]=
            org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
    }

    kswift {
        iosDeploymentTarget.set("14.0")
        install(dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature) {
            filter = includeFilter()//("ClassContext/ScrumBet:core/dev/luteoos/scrumbet/core/KState")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":data"))
                api(project(":domain"))
                implementation(Dependencies.kotlinStdlib)
                implementation(Dependencies.kotlinCoroutinesCore)
                implementation(Dependencies.kotlinSerializationJson)
                implementation(Dependencies.kotlinDatetime)
                implementation(Dependencies.reaktive)
                implementation(Dependencies.reaktiveCoroutinesInterop)
                implementation(Dependencies.multiplatformSettings)
                implementation(Dependencies.multiplatformSettingsSerialization)
                implementation(Dependencies.ktor)
                implementation(Dependencies.ktorSerialization)
                implementation(Dependencies.ktorAuth)
                implementation(Dependencies.ktorLogging)
                implementation(Dependencies.ktorContentNegotiation)

                api(Dependencies.koinCore)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(Dependencies.kotlinCoroutinesAndroid)
                implementation(Dependencies.ktorOkHttp)
                implementation(Dependencies.timber)
            }
        }
        val androidUnitTest by getting {
            dependencies{
                implementation(kotlin("test"))
                implementation("org.mockito:mockito-core:4.5.1")
                implementation(Dependencies.kotlinCoroutinesTest)
            }
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

            dependencies {
                implementation(Dependencies.ktorDarwin)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val macosArm64Main by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(Dependencies.ktorDarwin)
            }
        }
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "dev.luteoos.scrumbet"
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 28
        targetSdk = 32
    }

    flavorDimensionList.add("environment")

    productFlavors {
        create("localhost"){
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"192.168.18.3:8080\"")
        }
        create("azure"){
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"luteoos-scrumbet-poc.northeurope.cloudapp.azure.com:8080\"")
        }
        create("production"){
            isDefault = true
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"api.scrumhub.luteoos.dev\"")
        }
        forEach {
            it.buildConfigField("String", "APP_STORE_URL", "\"http://play.google.com/store/apps/details?id=dev.luteoos.scrumbet\"")
        }
    }
}

kotlin.targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java) {
    binaries.all {
        binaryOptions["memoryModel"] = "experimental"
        binaryOptions["freezing"] = "disabled"
    }
}

tasks.register("isKotlinNewMemory") {
//    println("is Kotlin New Memory Model(non-freeze) enabled: ${kotlin.native.isExperimentalMM()}") kotlin.native unresolved
}