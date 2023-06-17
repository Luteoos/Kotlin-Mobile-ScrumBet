//
//  MockRoomControllerInterface.swift
//  ScrumBet
//
//  Created by Mateusz Lutecki on 17/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import core

class MockRoomControllerInterfrace: RoomControllerInterface{
    var mockState: KState? = nil
    
    init(state: KState? = nil) {
        self.mockState = state
    }
    
    func watchState() -> CFlow<KState> {
        let mockValue = mockState ?? KStateSuccess(value:
                                                    RoomData(configuration:
                                                                RoomConfiguration(url: MultiUrl(base: "mockUrl.mock/mockRoomId"), isOwner: true, scale: ["1", "2", "3", "4", "5", "?"], scaleType: "Mock1", scaleTypeList: ["Mock1", "MOCK2"], voteEnded: false, anonymousVote: true, alwaysVisibleVote: true),
                                                             voteList: [RoomUser(userId: "mockUserId1", username: "mockUser1", isOwner: true, vote: nil)]))
        return CFlowCompanion().getMock(mockValue: mockValue as KState) as! CFlow<KState>
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
