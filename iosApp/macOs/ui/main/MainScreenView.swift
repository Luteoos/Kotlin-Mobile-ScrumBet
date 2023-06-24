//
//  MainScreenView.swift
//  macOs
//
//  Created by Mateusz Lutecki on 22/06/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import core
import SwiftUI

struct MainScreenView: View {
    @StateObject var object = MainScreenObject(controller: nil)
    @State var isEditNameVisible = false
    @State var isJoinSheetVisible = false
    @Environment(\.authController) var authController

    var body: some View {
        ZStack {
            Color.background

            VStack {
                Spacer()
                Text("hello, \(object.userData?.username ?? "")")
                    .font(.largeTitle)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 32)
                VStack {
                    Button {
                        isEditNameVisible.toggle()
                    } label: {
                        HStack {
                            if isEditNameVisible {
                                Image(systemName: "pencil.slash")
                                Text("close")
                            } else {
                                Image(systemName: "pencil")
                                Text("edit")
                            }
                        }
                    }
                    .buttonStyle(.borderless)
                    .tint(Color.secondaryColor)
                    Spacer()
                    if isEditNameVisible {
                        VStack {
                            MainScreenUsernameInputSheet(updateUsername: { username in
                                object.setUsername(username: username)
                            }, username: object.userData?.username ?? "", isVisible: $isEditNameVisible)
                        }
                        .frame(maxWidth: 400, maxHeight: 50)
                    }
                }
                .frame(height: 70)
                Spacer()

                VStack {
                    Button {
                        isJoinSheetVisible.toggle()
                    } label: {
                        Text("join")
                            .frame(maxWidth: .infinity)
                    }
                    .tint(Color.primaryColor)
                    .buttonStyle(.borderedProminent)
                    ZStack {
                        Divider()
                        Text("or")
                            .font(.caption)
                            .padding(.horizontal, 8)
                            .background(Color.backgroundColor)
                    }
                    Button {
                        object.createNewRoom()
                    } label: {
                        Text("create")
                            .bold()
                            .frame(maxWidth: .infinity)
                    }
                    .tint(Color.primaryColor)
                    .buttonStyle(.borderedProminent)
                }
                .frame(maxWidth: 200)
                .padding(.horizontal, 16)
                Spacer()
            }
            .onAppear {
                object.setAuthController(controller: authController)
            }
            .sheet(isPresented: $isJoinSheetVisible) {
                MainScreenJoinView(onJoin: { roomId in
                    object.setRoomId(id: roomId)
                }, isVisible: $isJoinSheetVisible)
            }
        }
    }
}

struct MainScreenView_Previews: PreviewProvider {
    static var previews: some View {
        MainScreenView(object: MainScreenObject(controller: MockUserController()))
            .environment(\.authController, MockAuthControllerInterface())
    }
}
