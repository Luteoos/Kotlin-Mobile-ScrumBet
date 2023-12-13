package dev.luteoos.scrumbet.controller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.mockito.MockitoAnnotations

abstract class BaseCoroutinesTest {

    @ExperimentalCoroutinesApi
    val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun resetDispatchers() {
        Dispatchers.resetMain()
    }
}
