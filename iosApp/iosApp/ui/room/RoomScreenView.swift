//
//  RoomScreenView.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 13/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import core

struct RoomScreenView: View {
    @Environment(\.authController) var authController
    @StateObject var object: RoomScreenObject = RoomScreenObject(controller: RoomController(roomRepository: nil, preferences: nil, baseUrl: nil))
    
    @State var isLoading = true
    @State var isShareSheetVisible = false
    
    var body: some View {
        VStack{
            switch object.state{
            case .Error(let error):
                RoomScreenErrorView(onRetry: {
                    object.connect()
                }, errorMessage: error)
            case .Success(let data):
                RoomScreenSuccessView(data: data, currentVote: object.currentVote) { style in
                    object.setRoomScale(scale: style)
                } onVote: { voteValue in
                    object.setVote(vote: voteValue)
                } onReset: {
                    object.reset()
                }

            case .Loading:
                ProgressView()
            }
        }
        .navigationTitle(object.title)
        .navigationBarTitleDisplayMode(.inline)
        .toolbar(content: {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button {
                    isShareSheetVisible.toggle()
                } label: {
                    Image(systemName: "square.and.arrow.up")
                }
                .disabled(isLoading)
            }
        })
        .sheet(isPresented: $isShareSheetVisible, content: {
            if let url = getUrl(){
                RoomScreenShareSheet(url: url)
            } else {
                EmptyView()
            }
        })
        .onAppear(){
            object.setAuthController(controller: authController)
            object.connect()
        }
        .onDisappear(){
            print("RoomScreen onDisappear")
            object.disconnect() // questionable
        }
        .onReceive(object.$state) { state in
            switch state{
                case .Success(data: _):
                    isLoading = false
                default:
                    isLoading = true
            }
            print("isLoading: \(isLoading)")
        }
        .onReceive(NotificationCenter.default.publisher(for: UIApplication.didBecomeActiveNotification)) { _ in
            print("didBecomeActiveNotification")
            if(!object.isAlive()){
                object.connect()
            }
        }
    }
    
    func getUrl() -> MultiUrl?{
        switch object.state{
        case .Success(let data):
            return data.configuration.url
        default:
            return nil
        }
    }
}

struct RoomScreenView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationWrapper(){
    
            RoomScreenView(object: RoomScreenObject(controller: MockRoomControllerInterfrace(state: KStateLoading())))
                .environment(\.authController, MockAuthControllerInterface())
        }
    }
}
