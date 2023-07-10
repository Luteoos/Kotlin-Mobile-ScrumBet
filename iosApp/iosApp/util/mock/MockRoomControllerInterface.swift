//
//  MockRoomControllerInterface.swift
//  ScrumBet
//
//  Created by Mateusz Lutecki on 17/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import core
import Foundation

class MockRoomControllerInterfrace: RoomControllerInterface {
    var mockState: KState? = nil

    init(state: KState? = nil) {
        mockState = state
    }

    func watchState() -> CFlow<KState> {
        let mockValue = mockState ?? KStateSuccess(value: getMockRoomData())
        return CFlowCompanion().getMock(mockValue: mockValue as KState) as! CFlow<KState>
    }

    func getMockRoomData() -> RoomData {
        return RoomData(configuration:
            RoomConfiguration(url: MultiUrl(base: "mockUrl.mock/mockRoomId"),"2137", isOwner: true, scale: ["1", "2", "3", "4", "5", "?"], scaleType: "MOCK1", scaleTypeList: ["MOCK1", "MOCK2"], voteEnded: false, anonymousVote: true, alwaysVisibleVote: true),
            voteList: [RoomUser(userId: "mockUserId1", username: "mockUser1", isOwner: true, vote: nil), RoomUser(userId: "mockUserId2", username: "mockUser2", isOwner: false, vote: "2")])
    }

    func connect(roomId: String) {
        print("connect \(roomId)")
    }

    func disconnect() {
        print("diconnect")
    }

    func displayValues(showValues: Bool) {
        print("displayValues change to \(showValues)")
    }

    func isSessionActive() -> Bool {
        return true
    }

    func resetRoom() {
        print("resetRoom")
    }

    func setRoomScale(scale: String) {
        print("setRoomScale to \(scale)")
    }

    func vote(voteValue: String) {
        print("vote voteValue: \(voteValue)")
    }

    func onDeInit() {
        print("deInit")
    }

    func onStart() {
        print("Start")
    }

    func onStop() {
        print("Stop")
    }
}
