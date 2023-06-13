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
    
    var body: some View {
        VStack{
            Text(userObject.userData?.username ?? "")
            Button {
                userObject.setUsername(username: "test")
            } label: {
                Text("test")
            }

        }
    }
}

struct MainScreenView_Previews: PreviewProvider {
    static var previews: some View {
        MainScreenView(userObject: MainScreenObject(controller: MockUserController()))
            .environmentObject(AuthObject(controller: MockAuthControllerInterface()))
    }
}
