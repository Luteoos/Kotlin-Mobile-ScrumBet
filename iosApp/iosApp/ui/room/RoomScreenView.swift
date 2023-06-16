//
//  RoomScreenView.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 13/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct RoomScreenView: View {
    @EnvironmentObject var authObject: AuthObject
    
    var body: some View {
        Text("Room Screen")
        Text("\(authObject.getRoomId() ?? "no id")")
        Button("Disconnect") {
            authObject.disconnect()
        }
    }
}

struct RoomScreenView_Previews: PreviewProvider {
    static var previews: some View {
        RoomScreenView()
            .environmentObject(AuthObject(controller: MockAuthControllerInterface()))
    }
}
