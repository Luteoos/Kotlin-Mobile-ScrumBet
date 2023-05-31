package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.controller.interfaces.RoomControllerInterface
import dev.luteoos.scrumbet.core.KController
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.core.launchDefault
import dev.luteoos.scrumbet.data.dto.RoomConfigOutgoingFrame
import dev.luteoos.scrumbet.data.dto.RoomResetOutgoingFrame
import dev.luteoos.scrumbet.data.dto.RoomVoteFrame
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.mapper.RoomDataMapper
import dev.luteoos.scrumbet.data.state.room.RoomConfiguration
import dev.luteoos.scrumbet.data.state.room.RoomData
import dev.luteoos.scrumbet.data.state.room.RoomUser
import dev.luteoos.scrumbet.domain.repository.interfaces.RoomRepository
import dev.luteoos.scrumbet.preferences.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.get

class RoomController(roomRepository: RoomRepository? = null, preferences: SharedPreferences? = null) : KController<RoomData, AppException>(), RoomControllerInterface {
    private val repository: RoomRepository
    private val sharedPreferences: SharedPreferences
    private val mapper: RoomDataMapper = RoomDataMapper()
    override val state: MutableStateFlow<KState<RoomData, AppException>> = MutableStateFlow(KState.Empty)

    init {
        repository = roomRepository ?: get()
        sharedPreferences = preferences ?: get()
    }

    override fun connect(roomId: String) {
        publish(KState.Loading)
        kcontrollerScope.launchDefault {
            val userData = sharedPreferences.getUserData()
            if (userData == null) {
                publish(KState.Error(AppException.GeneralException()))
                return@launchDefault
            }
            repository.initSession(roomId, userData.username, userData.userId).let { result ->
                when {
                    result.isFailure -> publish(KState.Error(AppException.GeneralException()))
                    result.isSuccess -> {
                        launch {
                            repository.getConnectionErrorFlow()
                                .collect {
                                    publish(KState.Error(it))
                                }
                        }
                        launchDefault {
                            repository.observeIncomingFlow()
                                .map {
                                    mapper.toRoomData(it, userData.userId)
                                }
                                .collect {
                                    publish(KState.Success(it))
                                }
                        }
                    }
                }
            }
        }
//        KHandler().postDelayed({
//            if (roomId == "true")
//                publish(KState.Success(getMockRoomData()))
//            else
//                publish(KState.Error(AppException.GeneralException("404")))
// //            for(i in 2..7){
// //                KHandler().postDelayed({
// //                    publish(KState.Success(getMockRoomData(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "11", "?").takeLast(i))))
// //                }, (i*2000).toLong())
// //            }
//        }, 1500)
    }

    override fun vote(voteValue: String) {
        val userData = sharedPreferences.getUserData() ?: return
        kcontrollerScope.launchDefault {
            repository.sendFrame(RoomVoteFrame(userData.userId, userData.username, voteValue))
        }
    }

    override fun resetRoom() {
        kcontrollerScope.launchDefault {
            repository.sendFrame(RoomResetOutgoingFrame())
        }
    }

    override fun displayValues(showValues: Boolean) {
        val userData = sharedPreferences.getUserData() ?: return
        val configuration = (state.value as? KState.Success<RoomData>)?.value?.configuration ?: return
        kcontrollerScope.launchDefault {
            repository.sendFrame(
                RoomConfigOutgoingFrame(
                    senderId = userData.userId,
                    scaleType = "FIBONACCI", // TODO implement proper room scale type
                    anonymousVote = configuration.anonymousVote,
                    alwaysVisibleVote = showValues
                )
            )
        }
    }

    override fun setRoomScale() {
        val userData = sharedPreferences.getUserData() ?: return
        val configuration = (state.value as? KState.Success<RoomData>)?.value?.configuration ?: return
        kcontrollerScope.launchDefault {
            repository.sendFrame(
                RoomConfigOutgoingFrame(
                    senderId = userData.userId,
                    scaleType = "FIBONACCI", // TODO implement proper room scale type
                    anonymousVote = configuration.anonymousVote,
                    alwaysVisibleVote = configuration.alwaysVisibleVote
                )
            )
        }
    }

    override fun disconnect() {
        println("Room controller disconnect")
        kcontrollerScope.launchDefault {
            repository.closeSession()
        }
    }

    private fun getMockRoomData(valuesList: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "11", "?")) =
        RoomData(
            RoomConfiguration(
                true,
                valuesList,
                voteEnded = false,
                anonymousVote = true,
                alwaysVisibleVote = false
            ),
            listOf(
                RoomUser(
                    "id1",
                    "user-user1",
                    false,
                    "2"
                ),
                RoomUser(
                    "id2",
                    "owner-user2",
                    true,
                    "4"
                )
            )
        )
}
