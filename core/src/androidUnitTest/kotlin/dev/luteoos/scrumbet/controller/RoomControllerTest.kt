package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.domain.repository.interfaces.RoomRepository
import dev.luteoos.scrumbet.preferences.SharedPreferences
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.mockito.Mock
import org.mockito.Mockito
import kotlin.test.Test
import kotlin.test.assertTrue

class RoomControllerTest : BaseCoroutinesTest() {

    @Mock
    lateinit var sharedPreferences: SharedPreferences
    @Mock
    lateinit var roomRepository: RoomRepository

    val url = "unitTest"

    @Test
    fun `General Exception as state`() = runTest(testDispatcher) {
        Mockito.`when`(sharedPreferences.getUserData()).thenReturn(null)

        val controller = RoomController(roomRepository, sharedPreferences, url)
        controller.connect("unitTestIdRoom")

        val list = controller.getStateFlow().take(2).toList()

        assertTrue { list.last() is KState.Error<AppException> }
        val value = list.last() as KState.Error<AppException>
        assertTrue { value.error is AppException.GeneralException }
    }
}
