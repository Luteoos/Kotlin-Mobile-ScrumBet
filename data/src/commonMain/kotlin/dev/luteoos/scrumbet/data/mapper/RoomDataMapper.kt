package dev.luteoos.scrumbet.data.mapper

import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.dto.RoomConfigIncomingFrame
import dev.luteoos.scrumbet.data.dto.RoomStateFrame
import dev.luteoos.scrumbet.data.dto.RoomVoteFrame
import dev.luteoos.scrumbet.data.state.room.RoomConfiguration
import dev.luteoos.scrumbet.data.state.room.RoomData
import dev.luteoos.scrumbet.data.state.room.RoomUser

class RoomDataMapper {

//    fun toRoomConfigurationFrame(data: RoomData) : RoomConfigOutgoingFrame{
//        return RoomConfigOutgoingFrame(
//
//        )
//    }

    fun toRoomData(frame: RoomStateFrame, userId: Id): RoomData {
        return RoomData(
            configuration = toRoomConfiguration(frame.config, userId),
            voteList = frame.list.map { toRoomUser(it, frame.config.roomOwnerId) }
        )
    }

    private fun toRoomConfiguration(frame: RoomConfigIncomingFrame, userId: Id): RoomConfiguration {
        return RoomConfiguration(
            frame.roomOwnerId == userId,
            frame.scaleList,
            frame.scaleType,
            frame.scaleTypeList,
            false,
            frame.anonymousVote,
            frame.alwaysVisibleVote
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
