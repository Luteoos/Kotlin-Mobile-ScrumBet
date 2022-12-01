package dev.luteoos.scrumbet.core

import kotlinx.coroutines.flow.StateFlow

interface KControllerInterface<stateData, stateError> {
    fun getStateFlow(): StateFlow<KState<stateData, stateError>>
    fun watchState(): CFlow<KState<stateData, stateError>>
    fun onStart()
    fun onStop()
    fun onDeInit()
}