//
//  ContentView.swift
//  ScrumBetMacOs
//
//  Created by Mateusz Lutecki on 21/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ContentView: View {
    @EnvironmentObject var authObject: AuthObject
    @State var navigate = false

    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundColor(Color.secondaryColor)
            Text("Hello, world! - macOS")
            Text(authObject.getUserData()?.description() ?? "empty")
            Text(authObject.getUserData()?.username ?? "empty")
            Button("navigate") {
                navigate.toggle()
            }

            NavigationLink("click") {
                VStack {
                    Text("o2 other")
                }
            }
        }
        .navigationDestination(isPresented: $navigate, destination: {
            VStack {
                Text("other")
            }
        })
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationWrapper {
            ContentView()
        }
        .environmentObject(AuthObject(controller: MockAuthControllerInterface()))
    }
}
