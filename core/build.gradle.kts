import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

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

    defaultConfigs {
        buildConfigField(STRING, "appVersion", "0.1", const = true)
        buildConfigField(STRING, "baseUrl", "my.url.here/controller", const = true)
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
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Core module with State control, exports as api() project(:data)"
        homepage = "luteoos.dev"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "core"
            export(project(":data"))
            transitiveExport = false
        }
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
                implementation(project(":domain"))
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
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 28
        targetSdk = 32
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