//
//  MockUserController.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 13/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import core

class MockUserController : UserControllerInterface{
    var mockState: KState? = nil
    
    init(state: KState? = nil) {
        self.mockState = state
    }
    
    func updateUsername(username: String) {
        print("mock updateUsername \(username)")
    }
    
    func onDeInit() {
    }
    
    func onStart() {
    }
    
    func onStop() {
    }
    
    func watchState() -> CFlow<KState> {
        let mockValue = mockState ?? KStateSuccess(value: UserData(username: "MockUser", userId: "mockId"))
        return CFlowCompanion().getMock(mockValue: mockValue as KState) as! CFlow<KState>
    }
    
    
}
