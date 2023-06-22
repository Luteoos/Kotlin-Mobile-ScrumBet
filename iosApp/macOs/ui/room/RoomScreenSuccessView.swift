//
//  RoomScreenSuccessView.swift
//  macOs
//
//  Created by Mateusz Lutecki on 22/06/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import SwiftUI

struct RoomScreenSuccessView: View {
    @ObservedObject var object: RoomScreenObject
    
    var body: some View {
        Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/)
    }
}

struct RoomScreenSuccessView_Previews: PreviewProvider {
    static var previews: some View {
        RoomScreenSuccessView(object: RoomScreenObject(controller: MockRoomControllerInterfrace()))
    }
}
