package dev.luteoos.scrumbet.core

import dev.luteoos.scrumbet.data.entity.AppException

/**
 * Based on [moko - ResourceState](https://github.com/icerockdev/moko-mvvm/blob/master/mvvm-state/src/commonMain/kotlin/dev/icerock/moko/mvvm/ResourceState.kt)
 */

sealed interface KState<out T, out E> {
    object Loading : KState<Nothing, Nothing>
    object Empty : KState<Nothing, Nothing>
    data class Success<T>(val value: T) : KState<T, Nothing>
    data class Error<E>(val error: Throwable) : KState<Nothing, E>
}

