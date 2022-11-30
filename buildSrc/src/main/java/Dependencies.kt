object Dependencies {
    val common = mapOf<String, String>(
        "kotlin.stdlib" to "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}",
        "kotlin.coroutines.core" to "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2",
        "kotlin.serialization.json" to "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3",
        "kotlin.datetime" to "org.jetbrains.kotlinx:kotlinx-datetime:0.3.2",
        "reaktive" to "com.badoo.reaktive:reaktive:${Versions.reaktive}",
        "reaktive.coroutines-interop" to "com.badoo.reaktive:coroutines-interop:${Versions.reaktive}",

        "rushwolf.multiplatform-settings-no-arg" to "com.russhwolf:multiplatform-settings-no-arg:${Versions.multiplatformSettings}",
        "rushwolf.multiplatform-settings-serialization" to "com.russhwolf:multiplatform-settings-serialization:${Versions.multiplatformSettings}"
    )
    val commonApi = mapOf<String, String>(
        "koinCore" to "io.insert-koin:koin-core:3.2.0"
    )
    val android = mapOf<String, String>(
        "kotlin.coroutines.android" to "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.2"
    )
    val androidUi = mapOf(
        "kotlin.android.coroutines-runtime" to "androidx.lifecycle:lifecycle-runtime-ktx:2.4.0"
    )
    val ios = mapOf<String, String>()

    /**
     * mapOf(<PodName, PodVersion>)
     */
    val iosPods = mapOf<String, String>()
}

object Versions {
    const val kotlin = "1.7.21"
    const val mokoKSwift =  "0.5.0"

    const val reaktive = "1.2.1"
    const val multiplatformSettings = "0.9"
}