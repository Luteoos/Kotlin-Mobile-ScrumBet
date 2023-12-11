package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.data.dto.ServerVersion
import dev.luteoos.scrumbet.domain.repository.interfaces.ServerRepository
import dev.luteoos.scrumbet.preferences.SharedPreferences
import dev.luteoos.scrumbet.shared.PlatformBuildConfigInterface
import kotlinx.coroutines.Dispatchers
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
    fun a() = runTest(UnconfinedTestDispatcher()) {
        `when`(sharedPreferences.getUserDataFlow()).thenReturn(MutableStateFlow(dev.luteoos.scrumbet.data.state.UserData("ut", "ut")))
        `when`(serverRepository.getServerVersionFlow()).thenReturn(flowOf(ServerVersion("1")))

        val controller = AuthController(sharedPreferences, serverRepository, buildConfig, "1")

        val list = controller.getStateFlow().take(2).toList()
//        runBlocking {
//            delay(1000)
//        }
//        controller.getStateFlow().value
//        assertTrue { list.last() is }
        list.get(0)
        assertEquals("ut", sharedPreferences.getUserDataFlow().value?.userId)
    }
}
