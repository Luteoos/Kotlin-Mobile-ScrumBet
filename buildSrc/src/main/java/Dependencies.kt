object Dependencies {
    // common
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val kotlinCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2"
    const val kotlinSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"
    const val kotlinDatetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val reaktive = "com.badoo.reaktive:reaktive:${Versions.reaktive}"
    const val reaktiveCoroutinesInterop = "com.badoo.reaktive:coroutines-interop:${Versions.reaktive}"

    const val multiplatformSettings = "com.russhwolf:multiplatform-settings-no-arg:${Versions.multiplatformSettings}"
    const val multiplatformSettingsSerialization = "com.russhwolf:multiplatform-settings-serialization:${Versions.multiplatformSettings}"

    const val ktor = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val ktorSerialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
    const val ktorAuth = "io.ktor:ktor-client-auth:${Versions.ktor}"
    const val ktorLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    const val ktorContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"

    // commonApi
    const val koinCore = "io.insert-koin:koin-core:${Versions.koin}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    // android
    const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.2"
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    const val ktorOkHttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"

    // androidUi
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activity}"
    const val activityKtx = "androidx.activity:activity-ktx:${Versions.activity}"

    const val kotlinAndroidCoroutinesRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
    const val accompanistMDC = "com.google.accompanist:accompanist-themeadapter-material3:0.29.0-alpha"
    const val accompanistPermissions = "com.google.accompanist:accompanist-permissions:0.30.1"
    const val material = "com.google.android.material:material:1.9.0"

    const val M3 = "androidx.compose.material3:material3:${Versions.material}"
    const val M3WindowSize = "androidx.compose.material3:material3-window-size-class:${Versions.material}"
    const val composeUi = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeRuntime = "androidx.compose.runtime:runtime-livedata:${Versions.compose}"
    const val composeTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composePreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    const val composeFoundtaion = "androidx.compose.foundation:foundation:${Versions.compose}"


    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    //iOS
    const val ktorDarwin = "io.ktor:ktor-client-darwin:${Versions.ktor}"
    const val ktorCio = "io.ktor:ktor-client-cio:${Versions.ktor}"

    //iOS pods
}

object Versions {
    const val kotlin = "1.8.0"
    const val mokoKSwift =  "0.5.0"
    const val timber = "4.7.1"

    const val reaktive = "1.2.1"
    const val multiplatformSettings = "0.9"
    const val koin = "3.2.2"
    const val ktor = "2.3.1"

    const val compose = "1.4.3"
    const val material = "1.1.1"
    const val navigation = "2.6.0"
    const val activity = "1.7.2"
}