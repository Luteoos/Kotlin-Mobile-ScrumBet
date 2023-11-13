package dev.luteoos.scrumbet.data.mapper

import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.dto.RoomConfigIncomingFrame
import dev.luteoos.scrumbet.data.dto.RoomStateFrame
import dev.luteoos.scrumbet.data.dto.RoomVoteFrame
import dev.luteoos.scrumbet.data.entity.MultiUrl
import dev.luteoos.scrumbet.data.state.room.RoomConfiguration
import dev.luteoos.scrumbet.data.state.room.RoomData
import dev.luteoos.scrumbet.data.state.room.RoomUser

class RoomDataMapper {

//    fun toRoomConfigurationFrame(data: RoomData) : RoomConfigOutgoingFrame{
//        return RoomConfigOutgoingFrame(
//
//        )
//    }

    fun toRoomData(frame: RoomStateFrame, userId: Id, baseUrl: String, roomId: String): RoomData {
        return RoomData(
            configuration = toRoomConfiguration(frame.config, userId, MultiUrl("${baseUrl.split(":").first()}/$roomId")),
            voteList = frame.list.map { toRoomUser(it, frame.config.roomOwnerId) }
        )
    }

    private fun toRoomConfiguration(frame: RoomConfigIncomingFrame, userId: Id, url: MultiUrl): RoomConfiguration {
        return RoomConfiguration(
            url,
            frame.roomCode,
            frame.roomOwnerId == userId,
            frame.scaleList,
            frame.scaleType,
            frame.scaleTypeList,
            frame.voteEnded,
            frame.anonymousVote,
            frame.alwaysVisibleVote,
            frame.autoRevealVotes
        )
    }

    private fun toRoomUser(frame: RoomVoteFrame, ownerId: Id): RoomUser {
        return RoomUser(
            frame.userId,
            frame.username,
            isOwner = ownerId == frame.userId,
            vote = frame.vote
        )
    }
}
