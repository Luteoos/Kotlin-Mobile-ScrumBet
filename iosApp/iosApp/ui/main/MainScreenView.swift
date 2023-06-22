//
//  MainScreenView.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 13/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import core

struct MainScreenView: View {
    @StateObject var object = MainScreenObject(controller: nil)
    @State var isEditNameSheetVisible = false
    @State var isJoinSheetVisible = false
    @Environment(\.authController) var authController
    
    var body: some View {
        ZStack{
            Color.background
            
            VStack{
                Spacer()
                Text("hello, \(object.userData?.username ?? "")")
                    .font(.largeTitle)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 32)
                Button {
                    isEditNameSheetVisible.toggle()
                } label: {
                    HStack{
                        Image(systemName: "pencil")
                        Text("edit")
                    }
                }
                    .buttonStyle(.borderless)
                    .tint(Color.secondaryColor)
                Spacer()
                
                VStack{
                    Button{
                        isJoinSheetVisible.toggle()
                    } label: {
                        Text("join")
                            .frame(maxWidth: .infinity)
                    }
                    .tint(Color.primaryColor)
                    .buttonStyle(.borderedProminent)
                    Text("or")
                        .font(.caption)
                    Button{
                        object.createNewRoom()
                    } label: {
                        Text("create")
                            .bold()
                            .frame(maxWidth: .infinity)
                    }
                    .tint(Color.primaryColor)
                    .buttonStyle(.borderedProminent)
                }
                    .padding(.horizontal, 16)
                Spacer()
            }
            .onAppear(){
                object.setAuthController(controller: authController)
            }
            .sheet(isPresented: $isEditNameSheetVisible, content: {
                HalfSheet {
                    MainScreenUsernameInputSheet(
                        updateUsername: { username in
                            object.setUsername(username: username)
                        },
                        username: object.userData?.username ?? "",
                        isVisible: $isEditNameSheetVisible
                    )
                    
                }
            })
            .sheet(isPresented: $isJoinSheetVisible, content: {
                HalfSheet {
                    MainScreenJoinSheet(joinRoom: { roomId in
                        object.setRoomId(id: roomId)
                    }, isVisible: $isJoinSheetVisible)
                }
            })
        }
    }
}

struct MainScreenView_Previews: PreviewProvider {
    static var previews: some View {
        MainScreenView(object: MainScreenObject(controller: MockUserController()))
            .environment(\.authController, MockAuthControllerInterface())
    }
}
