//
//  RoomScreenSuccessView.swift
//  ScrumBet
//
//  Created by Mateusz Lutecki on 18/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import core

struct RoomScreenSuccessView: View {
    @StateObject var object: RoomScreenObject
    
    var body: some View {
        switch object.state{
        case .Success(let data):
            VStack{
                Text(data.configuration.scaleType)
            }
        default:
            EmptyView()
        }
    }
}

struct RoomScreenSuccessView_Previews: PreviewProvider {
    static var previews: some View {
        RoomScreenSuccessView(object: RoomScreenObject(controller: MockRoomControllerInterfrace()))
    }
}
