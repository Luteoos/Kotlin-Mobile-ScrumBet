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
    
    var body: some View {
        VStack{
            switch object.state{
            case .Error(let error):
                EmptyView()
            case .Success(let data):
                EmptyView()
            case .Loading:
                ProgressView()
            }
        }
        .navigationTitle(object.title)
        .navigationBarTitleDisplayMode(.inline)
        .toolbar(content: {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button {
                    print("shareSheet")
                } label: {
                    Image(systemName: "square.and.arrow.up")
                }
                .disabled(isLoading)
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
}

struct RoomScreenView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationWrapper(){
    
            RoomScreenView(object: RoomScreenObject(controller: MockRoomControllerInterfrace(state: KStateLoading())))
                .environment(\.authController, MockAuthControllerInterface())
        }
    }
}
