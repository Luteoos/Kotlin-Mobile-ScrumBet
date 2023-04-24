package dev.luteoos.scrumbet.core

import kotlinx.coroutines.flow.StateFlow
import kotlin.experimental.ExperimentalObjCRefinement
import kotlin.native.HiddenFromObjC

interface KControllerInterface<stateData, stateError> {
    @OptIn(ExperimentalObjCRefinement::class)
    @HiddenFromObjC
    fun getStateFlow(): StateFlow<KState<stateData, stateError>>
    fun watchState(): CFlow<KState<stateData, stateError>>
    fun onStart()
    fun onStop()
    fun onDeInit()
}
