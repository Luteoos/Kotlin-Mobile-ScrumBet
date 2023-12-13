package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.state.UserData
import dev.luteoos.scrumbet.preferences.SharedPreferences
import dev.luteoos.scrumbet.shared.DeviceData
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserControllerTest : BaseCoroutinesTest() {

    @Mock
    lateinit var sharedPreferences: SharedPreferences

    @Test
    fun `User id and name is in state`() = runTest(testDispatcher) {
        Mockito.`when`(sharedPreferences.getUserData()).thenReturn(UserData("unitTestUser", "unitTestId"))
        val controller = UserController(sharedPreferences, deviceData = DeviceData())

        val list = controller.getStateFlow().take(1).toList()

        assertTrue { list.last() is KState.Success<UserData> }
        val last = (list.last() as KState.Success<UserData>)
        assertEquals("unitTestUser", last.value.username)
        assertEquals("unitTestId", last.value.userId)
    }
}
