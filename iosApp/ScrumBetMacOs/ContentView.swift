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
    
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundColor(.accentColor)
            Text("Hello, world! - macOS")
            Text(authObject.getUserData()?.description() ?? "empty")
            Text(authObject.getUserData()?.username ?? "empty")
        }
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
