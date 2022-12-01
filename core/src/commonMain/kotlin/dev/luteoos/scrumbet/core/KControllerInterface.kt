package dev.luteoos.scrumbet.core

interface KControllerInterface<stateData, stateError> {
    fun watchState(): CFlow<KState<stateData, stateError>>
}