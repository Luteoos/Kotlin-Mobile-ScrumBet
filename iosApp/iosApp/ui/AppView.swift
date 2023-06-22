//
//  AppView.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 15/06/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import core

struct AppView: View {
    @EnvironmentObject var authObject: AuthObject
    @State var isErrorDisplayed = false
    @State var uiState: AuthUIState? = nil
    
    var body: some View {
        
        NavigationWrapper(){
            ZStack{
                Color.background
                
                if(authObject.state == .Loading){
                    ProgressView()
                }
                else if(authObject.state == .InvalidAppVersion){
                    ProgressView() // force update app
                }
                else{
                    MainScreenView()
                }
                
                NavigationLink( destination: RoomScreenView(), tag: AuthUIState.Connected, selection: $uiState){ EmptyView() }
            }
        }
        .alert("error", isPresented: $isErrorDisplayed, actions: {
            Button("retry") {
                authObject.retry()
            }
        })
        .onReceive(authObject.$state) { inState in
            isErrorDisplayed = inState == .GeneralError
            uiState = inState
        }
    }
}

struct AppView_Previews: PreviewProvider {
   static var previews: some View {
       AppView()
           .environmentObject(AuthObject(controller: MockAuthControllerInterface(state: KStateEmpty())))
   }
}
