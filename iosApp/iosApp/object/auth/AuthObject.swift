//
//  AuthObject.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 12/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import core
import Foundation

class AuthObject: ObservableObject {
    private let controller: AuthControllerInterface
    @Published var state: AuthUIState = .Loading

    init(controller: AuthControllerInterface) {
        self.controller = controller
        self.controller.onStart()
        watchFlows()
    }

    private func watchFlows() {
        controller.watchState().watch { [weak self] inState in
            switch KStateSwift<AuthState, AppException>(inState) {
            case .empty:
                self?.state = .EmptyUserData
            case .error:
                self?.state = .GeneralError
            case .loading:
                self?.state = .Loading
            case let .success(authState):
                switch authState.value {
                case is AuthState.Connected:
                    self?.state = .Connected
                case is AuthState.UserSignedIn:
                    self?.state = .UserSigned
                case is AuthState.InvalidVersion:
                    self?.state = .InvalidAppVersion
                default:
                    self?.state = .GeneralError
                }
            }
        }
    }

    func createNewRoom() {
        setRoomId(NSUUID().uuidString)
    }

    func setRoomId(_ id: String) {
        controller.setRoomConnectionId(id: id)
    }

    func retry() {
        controller.retry()
    }

    func disconnect() {
        controller.disconnect()
    }

    func getUserData() -> UserData? {
        return controller.getUserData()
    }

    func getRoomId() -> String? {
        return controller.getRoomConnectionId()
    }

    deinit {
        controller.onDeInit()
    }
}
