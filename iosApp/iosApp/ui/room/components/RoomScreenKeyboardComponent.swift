//
//  RoomScreenKeyboardComponent.swift
//  macOs
//
//  Created by Mateusz Lutecki on 22/06/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import SwiftUI

struct RoomScreenKeyboardComponent: View {
    @ObservedObject var object: RoomScreenObject

    var body: some View {
        ScrollView {
            if case let .Success(data) = object.state {
                LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible()), GridItem(.flexible())]) {
                    ForEach(data.configuration.scale, id: \.self) { text in
                        Button {
                            object.setVote(vote: text)
                        } label: {
                            ZStack {
                                if object.currentVote == nil || text == object.currentVote {
                                    Color.primaryColor
                                } else {
                                    Color.primaryColor.opacity(0.4)
                                }
                                Text(text)
                                    .tint(Color.black)
                            }
                            .frame(width: 64, height: 64)
                            .cornerRadius(16)
                        }
                        #if os(macOS)
                        .buttonStyle(.plain)
                        #endif
                    }
                }
                .frame(width: 250)
                .padding(.top, 16)
            }
        }
    }
}

struct RoomScreenKeyboardComponent_Previews: PreviewProvider {
    static var previews: some View {
        RoomScreenKeyboardComponent(object: RoomScreenObject(controller: MockRoomControllerInterfrace()))
    }
}
