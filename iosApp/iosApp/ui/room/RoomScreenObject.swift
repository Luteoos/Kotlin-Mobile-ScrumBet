//
//  RoomScreenObject.swift
//  ScrumBet
//
//  Created by Mateusz Lutecki on 17/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import core

class RoomScreenObject: ObservableObject{
    private let controller: RoomControllerInterface
    private var authController: AuthControllerInterface? = nil
    @Published var state: RoomUiState = .Loading
    @Published var title: String = ""
    @Published var currentVote: String? = nil
    @Published var votes: [RoomUser] = []
    @Published var configuration: RoomConfiguration? = nil
    
    init(controller: RoomControllerInterface?) {
        self.controller = controller ?? RoomController(roomRepository: nil, preferences: nil, baseUrl: nil)
        self.controller.onStart()
        self.controller.watchState().watch {[weak self] inState in
            switch KStateSwift<RoomData, AppException>(inState){
            case .success(let data):
                print(data.value?.description() ?? "no userData")
                self?.title = "\(data.value!.voteList.first(where: \.isOwner)?.username ?? "Error")'s room"
                self?.state = .Success(data: data.value!)
                self?.currentVote = data.value!.voteList.first { user in
                    self?.authController?.getUserData()?.userId == user.userId
                }?.vote
                self?.votes = data.value!.voteList
                self?.configuration = data.value?.configuration
            case .error(let error):
                print(error.error.description())
                self?.title = "\(error.error.description())"
                self?.state = .Error(error: error.error.description())
            default:
                self?.title = ""
                self?.state = .Loading
            }
        }
    }
    
    func setAuthController(controller: AuthControllerInterface){
        self.authController = controller
    }
    
    func connect(){
        guard let roomId = authController!.getRoomConnectionId() else {
            state = .Error(error: "error_room_id")
            return
        }
        controller.connect(roomId: roomId)
    }
    
    func disconnect(){
        authController?.disconnect()
        controller.disconnect()
    }
    
    func isAlive() -> Bool{
        return controller.isSessionActive()
    }
    
    func setVote(vote: String){
        controller.vote(voteValue: vote)
    }
    
    func setRoomScale(scale: String){
        controller.setRoomScale(scale: scale)
    }
    
    func showVoteVisibility(_ visible: Bool){
        controller.displayValues(showValues: visible)
    }
    
    func reset(){
        controller.resetRoom()
    }
}
