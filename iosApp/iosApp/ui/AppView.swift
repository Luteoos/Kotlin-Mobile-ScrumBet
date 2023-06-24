//
//  AppView.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 15/06/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import core
import SwiftUI

struct AppView: View {
    @EnvironmentObject var authObject: AuthObject
    @State var isErrorDisplayed = false
    @State var isConnected = false

    var body: some View {
        NavigationWrapper {
            ZStack {
                Color.background

                if authObject.state == .Loading {
                    ProgressView()
                } else if authObject.state == .InvalidAppVersion {
                    ProgressView() // force update app
                } else {
                    MainScreenView()
                }
            }
            .navigationDestination(isPresented: $isConnected, destination: {
                RoomScreenView()
            })
        }
        .alert("error", isPresented: $isErrorDisplayed, actions: {
            Button("retry") {
                authObject.retry()
            }
        })
        .onReceive(authObject.$state) { inState in
            isErrorDisplayed = inState == .GeneralError
            isConnected = inState == .Connected
        }
    }
}

struct AppView_Previews: PreviewProvider {
    static var previews: some View {
        AppView()
            .environmentObject(AuthObject(controller: MockAuthControllerInterface(state: KStateEmpty())))
    }
}
