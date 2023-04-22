package dev.luteoos.scrumbet.core

import kotlinx.coroutines.flow.StateFlow

abstract class KControllerInterface<stateData, stateError> {
    abstract fun getStateFlow(): StateFlow<KState<stateData, stateError>>
    abstract fun watchState(): CFlow<KState<stateData, stateError>>
    abstract fun onStart()
    abstract fun onStop()
    abstract fun onDeInit()
}
