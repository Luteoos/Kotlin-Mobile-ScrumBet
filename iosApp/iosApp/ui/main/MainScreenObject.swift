//
//  MainScreenObject.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 13/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import core

class MainScreenObject : ObservableObject{
    private let controller: UserControllerInterface
    @Published var userData: UserData? = nil
    
    init(controller: UserControllerInterface?) {
        self.controller = controller ?? UserController(preferences: nil, deviceData: nil)
        self.controller.onStart()
        self.controller.watchState().watch {[weak self] inState in
            switch KStateSwift<UserData, AppException>(inState){
            case .success(let data):
                print(data.value?.description() ?? "no userData")
                self?.userData = data.value
            default:
                self?.userData = nil
            }
        }
    }
    
    func setUsername(username: String){
        controller.updateUsername(username: username)
    }
}
