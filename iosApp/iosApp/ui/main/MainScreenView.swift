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
    @StateObject var userObject = MainScreenObject(controller: nil)
    @EnvironmentObject var authObject: AuthObject
    @State var isEditNameSheetVisible = false
    
    var body: some View {
        ZStack{
            Color.background
            
            VStack{
                Spacer()
                Text("hello, \(userObject.userData?.username ?? "")")
                    .font(.largeTitle)
                Button {
//                    userObject.setUsername(username: "test")
                    isEditNameSheetVisible.toggle()
                } label: {
                    HStack{
                        Image(systemName: "pencil")
                        Text("edit")
                    }
                }
                    .buttonStyle(.borderless)
                Spacer()
                
                VStack{
                    Button{
                        
                    } label: {
                        Text("join")
                            .frame(maxWidth: .infinity)
                    }
                    .tint(Color.primaryColor)
                    .buttonStyle(.borderedProminent)
                    Text("or")
                        .font(.caption)
                    Button{
                        
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
            .sheet(isPresented: $isEditNameSheetVisible, content: {
                HalfSheet {
                    MainScreenUsernameInputSheet(
                        updateUsername: { username in
                            userObject.setUsername(username: username)
                        },
                        username: userObject.userData?.username ?? "",
                        isVisible: $isEditNameSheetVisible
                    )
                    
                }
            })
        }
    }
}

struct MainScreenView_Previews: PreviewProvider {
    static var previews: some View {
        MainScreenView(userObject: MainScreenObject(controller: MockUserController()))
            .environmentObject(AuthObject(controller: MockAuthControllerInterface()))
    }
}
