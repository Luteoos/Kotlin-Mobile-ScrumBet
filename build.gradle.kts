buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.3")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.13.3")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.5")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://jitpack.io"))
    }
}

plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.3.1").apply(false)
    id("com.android.library").version("7.3.1").apply(false)
    kotlin("android").version(Versions.kotlin).apply(false)
    kotlin("multiplatform").version(Versions.kotlin).apply(false)
    id("org.sonarqube") version "4.3.0.3225"
}

sonar {
    properties {
        property("sonar.projectKey", "luteoos-dev_mobile-scrumhub")
        property("sonar.organization", "luteoos-dev")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.sources", "./")
        property("sonar.exclusions", "**/build/**, **/core/build/**, **/build.gradle.kts, **/src/test/**, **/google-services.json, **/GoogleService-Info.plist, **/xcuserdata")

    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
