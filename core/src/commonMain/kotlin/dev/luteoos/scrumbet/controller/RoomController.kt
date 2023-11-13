package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.controller.interfaces.RoomControllerInterface
import dev.luteoos.scrumbet.core.KController
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.core.launchDefault
import dev.luteoos.scrumbet.data.dto.RoomConfigOutgoingFrame
import dev.luteoos.scrumbet.data.dto.RoomResetOutgoingFrame
import dev.luteoos.scrumbet.data.dto.RoomVoteFrame
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.entity.MultiUrl
import dev.luteoos.scrumbet.data.mapper.RoomDataMapper
import dev.luteoos.scrumbet.data.state.room.RoomConfiguration
import dev.luteoos.scrumbet.data.state.room.RoomData
import dev.luteoos.scrumbet.data.state.room.RoomUser
import dev.luteoos.scrumbet.domain.repository.interfaces.RoomRepository
import dev.luteoos.scrumbet.preferences.SharedPreferences
import dev.luteoos.scrumbet.shared.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.get
import org.koin.core.qualifier.named

class RoomController(roomRepository: RoomRepository? = null, preferences: SharedPreferences? = null, baseUrl: String? = null) : KController<RoomData, AppException>(), RoomControllerInterface {
    private val repository: RoomRepository
    private val sharedPreferences: SharedPreferences
    private val url: String
    override val state: MutableStateFlow<KState<RoomData, AppException>> = MutableStateFlow(KState.Empty)
    private val mapper: RoomDataMapper = RoomDataMapper()

    init {
        repository = roomRepository ?: get()
        sharedPreferences = preferences ?: get()
        url = baseUrl ?: get(named("BASE_URL"))
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
                                    mapper.toRoomData(it, userData.userId, url, roomId)
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
                    scaleType = configuration.scaleType,
                    anonymousVote = configuration.anonymousVote,
                    alwaysVisibleVote = showValues,
                    voteEnded = configuration.voteEnded,
                    autoRevealVotes = configuration.autoRevealVotes
                )
            )
        }
    }

    override fun setAutoRevealVotes(autoReveal: Boolean) {
        val userData = sharedPreferences.getUserData() ?: return
        val configuration = (state.value as? KState.Success<RoomData>)?.value?.configuration ?: return
        kcontrollerScope.launchDefault {
            repository.sendFrame(
                RoomConfigOutgoingFrame(
                    senderId = userData.userId,
                    scaleType = configuration.scaleType,
                    anonymousVote = configuration.anonymousVote,
                    alwaysVisibleVote = configuration.alwaysVisibleVote,
                    voteEnded = configuration.voteEnded,
                    autoRevealVotes = autoReveal
                )
            )
        }
    }

    override fun endVote() {
        val userData = sharedPreferences.getUserData() ?: return
        val configuration = (state.value as? KState.Success<RoomData>)?.value?.configuration ?: return
        kcontrollerScope.launchDefault {
            repository.sendFrame(
                RoomConfigOutgoingFrame(
                    senderId = userData.userId,
                    scaleType = configuration.scaleType,
                    anonymousVote = configuration.anonymousVote,
                    alwaysVisibleVote = configuration.alwaysVisibleVote,
                    voteEnded = true, //configuration.voteEnded,
                    autoRevealVotes = configuration.autoRevealVotes
                )
            )
        }
    }

    override fun setRoomScale(scale: String) {
        val userData = sharedPreferences.getUserData() ?: return
        val configuration = (state.value as? KState.Success<RoomData>)?.value?.configuration ?: return
        if (configuration.scaleTypeList.contains(scale).not())
            return
        kcontrollerScope.launchDefault {
            repository.sendFrame(
                RoomConfigOutgoingFrame(
                    senderId = userData.userId,
                    scaleType = scale,
                    anonymousVote = configuration.anonymousVote,
                    alwaysVisibleVote = configuration.alwaysVisibleVote,
                    voteEnded = false,  //configuration.voteEnded,
                    autoRevealVotes = configuration.autoRevealVotes
                )
            )
        }
    }

    override fun disconnect() {
        Log.d("Room controller disconnect")
        kcontrollerScope.launchDefault {
            repository.closeSession()
        }
    }

    override fun isSessionActive(): Boolean {
        return repository.isSessionActive()
    }

    private fun getMockRoomData(valuesList: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "11", "?")) =
        RoomData(
            RoomConfiguration(
                MultiUrl("placeholder.com/placeholder"),
                "2137",
                true,
                valuesList,
                "FIBONACCI",
                listOf("FIBONACCI", "HOURS"),
                voteEnded = false,
                anonymousVote = true,
                alwaysVisibleVote = false,
                autoRevealVotes = true,
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
