object Dependencies {
    // common
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val kotlinCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2"
    const val kotlinSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"
    const val kotlinDatetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"
    const val reaktive = "com.badoo.reaktive:reaktive:${Versions.reaktive}"
    const val reaktiveCoroutinesInterop = "com.badoo.reaktive:coroutines-interop:${Versions.reaktive}"

    const val multiplatformSettings = "com.russhwolf:multiplatform-settings-no-arg:${Versions.multiplatformSettings}"
    const val multiplatformSettingsSerialization = "com.russhwolf:multiplatform-settings-serialization:${Versions.multiplatformSettings}"

    // commonApi
    const val koinCore = "io.insert-koin:koin-core:3.2.2"

    // android
    const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.2"

    // androidUi
    const val kotlinAndroidCoroutinesRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0"

    //iOS

    /**
     * mapOf(<PodName, PodVersion>)
     */
    val iosPods = mapOf<String, String>()
}

object Versions {
    const val kotlin = "1.8.0"
    const val mokoKSwift =  "0.5.0"

    const val reaktive = "1.2.1"
    const val multiplatformSettings = "0.9"
}