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
    
    var body: some View {
        ZStack{
            Color.background
            
            VStack{
                switch authObject.state {
                case .Connected:
                    AnyView(ContentView())// room screen
                case .UserSigned, .EmptyUserData:
                    ContentView() // join screen
                case .InvalidAppVersion:
                    AnyView(ContentView()) // update app screen
                case .Loading:
                    ProgressView()
                default:
                    ProgressView()
                }
            }.alert("error", isPresented: $isErrorDisplayed, actions: {
                Button("retry") {
                    authObject.retry()
                }
            })
            .onReceive(authObject.$state) { obj in
                isErrorDisplayed = obj == .GeneralError
            }
        }
    }
}

struct AppView_Previews: PreviewProvider {
   static var previews: some View {
       AppView()
           .environmentObject(AuthObject(controller: MockAuthControllerInterface()))
//           .environmentObject(AuthObject(controller: AuthController(preferences: nil, serverRepository: ServerRepositoryImpl(baseUrl: "luteoos-scrumbet-poc.northeurope.cloudapp.azure.com:8080", sslPrefix: "http://", httpClient: HttpClientProviderKt.getHttpClient()), applicationVersion: nil)))
   }
}
