//
//  MockAuthControllerInterface.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 12/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import core

class MockAuthControllerInterface : AuthControllerInterface{
    func disconnect() {
        print("mock disconnect()")
    }
    
    func getRoomConnectionId() -> String? {
        return "mock room id"
    }
    
    func getUserData() -> UserData? {
        return UserData(username: "MockUser", userId: "MockId")
    }
    
    func retry() {
        print("mock retry()")
    }
    
    func setRoomConnectionId(id: String) {
        print("mock setRoomConnectionId ")
    }
    
    func onDeInit() {
        print("onDeinit")
    }
    
    func onStart() {
        print("mock onStart")
    }
    
    func onStop() {
        
    }
    
    func watchState() -> CFlow<KState> {
        let mockValue = KStateError<AppException>(error: AppException.GeneralException.init(message: "mockError"))
        return CFlowCompanion().getMock(mockValue: mockValue as KState) as! CFlow<KState>
    }
    
    
}
