@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.dto.ServerVersion
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.state.AuthState
import dev.luteoos.scrumbet.domain.repository.interfaces.ServerRepository
import dev.luteoos.scrumbet.preferences.SharedPreferences
import dev.luteoos.scrumbet.shared.PlatformBuildConfigInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthControllerTest {

    @Mock
    lateinit var sharedPreferences: SharedPreferences
    @Mock
    lateinit var serverRepository: ServerRepository
    @Mock
    lateinit var buildConfig: PlatformBuildConfigInterface

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

    @Test
    fun `User signed in published to state`() = runTest(UnconfinedTestDispatcher()) {
        `when`(sharedPreferences.getUserDataFlow()).thenReturn(MutableStateFlow(dev.luteoos.scrumbet.data.state.UserData("ut", "ut")))
        `when`(serverRepository.getServerVersionFlow()).thenReturn(flowOf(ServerVersion("1")))

        val controller = AuthController(sharedPreferences, serverRepository, buildConfig, "1")

        val list = controller.getStateFlow().take(2).toList()
        assertTrue { list.last() is KState.Success<AuthState> &&
                (list.last() as KState.Success<AuthState>).value is AuthState.UserSignedIn }
//        assertEquals("ut", sharedPreferences.getUserDataFlow().value?.userId)
    }

    @Test
    fun `Invalid version published to state`() = runTest(UnconfinedTestDispatcher()) {
        `when`(sharedPreferences.getUserDataFlow()).thenReturn(MutableStateFlow(dev.luteoos.scrumbet.data.state.UserData("ut", "ut")))
        `when`(serverRepository.getServerVersionFlow()).thenReturn(flowOf(ServerVersion("2")))

        val controller = AuthController(sharedPreferences, serverRepository, buildConfig, "1")

        val list = controller.getStateFlow().take(2).toList()
        assertTrue { list.last() is KState.Success<AuthState> &&
                (list.last() as KState.Success<AuthState>).value is AuthState.InvalidVersion }
    }

    @Test
    fun `Connected published to state`() = runTest(UnconfinedTestDispatcher()) {
        `when`(sharedPreferences.getUserDataFlow()).thenReturn(MutableStateFlow(dev.luteoos.scrumbet.data.state.UserData("ut", "ut")))
        `when`(serverRepository.getServerVersionFlow()).thenReturn(flowOf(ServerVersion("1")))

        val controller = AuthController(sharedPreferences, serverRepository, buildConfig, "1")

        controller.setRoomConnectionId("unitTest")

        val list = controller.getStateFlow().take(2).toList()

        assertTrue { list.last() is KState.Success<AuthState> &&
                (list.last() as KState.Success<AuthState>).value is AuthState.Connected }

//        controller.retry()
//        val listRetry = controller.getStateFlow().take(1).toList()
//        listRetry.get(0)

    }

    @Test
    fun `General exception published to state`() = runTest(UnconfinedTestDispatcher()) {
        `when`(sharedPreferences.getUserDataFlow()).thenReturn(MutableStateFlow(dev.luteoos.scrumbet.data.state.UserData("ut", "ut")))
        `when`(serverRepository.getServerVersionFlow()).thenReturn(flowOf(null))

        val controller = AuthController(sharedPreferences, serverRepository, buildConfig, "1")

        val list = controller.getStateFlow().take(2).toList()


        assertTrue { list.last() is KState.Error<AppException> &&
                (list.last() as KState.Error<AppException>).error is AppException.GeneralException }
    }

    @Test
    fun `Room ID is correctly stripped `() = runTest(UnconfinedTestDispatcher()) {
        `when`(sharedPreferences.getUserDataFlow()).thenReturn(MutableStateFlow(dev.luteoos.scrumbet.data.state.UserData("ut", "ut")))
        `when`(serverRepository.getServerVersionFlow()).thenReturn(flowOf(null))

        val controller = AuthController(sharedPreferences, serverRepository, buildConfig, "1")

        controller.setRoomConnectionId("toStrip/tobeStripped/unitTest")
        assertEquals("unitTest", controller.getRoomConnectionId())
    }
}
