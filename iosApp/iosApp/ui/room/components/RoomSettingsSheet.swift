//
//  RoomSettingsSheet.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 13/07/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import SwiftUI

struct RoomSettingsSheet: View {
    @ObservedObject var object: RoomScreenObject

    @State private var currentStyle = ""
    @State private var showingVotes = false
    
    var body: some View {
        if case let .Success(data) = object.state {
            ScrollView {
                HStack{
                    Toggle(isOn: $showingVotes) {
                        Text("vote_visible_label")
                    }
                    .toggleStyle(.switch)
                    .tint(Color.primaryColor)
                }
                HStack {
                    #if os(iOS)
                    Text("vote_style_label")
                    Spacer()
                    #endif
                    Picker("vote_style_label", selection: $currentStyle) {
                        ForEach(data.configuration.scaleTypeList, id: \.self) {
                            Text($0.localizedCapitalized).tag($0)
                        }
                    }
                    .pickerStyle(.menu)
                    .tint(Color.primary)
                }
            }
            .padding(16)
            .onChange(of: currentStyle) { scaleStyle in
                object.setRoomScale(scale: scaleStyle)
            }
            .onChange(of: showingVotes) { showVotes in
                object.showVoteVisibility(showVotes)
            }
            .onReceive(object.$state, perform: { state in
                if case let .Success(data) = state {
                    currentStyle = data.configuration.scaleType
                    showingVotes = data.configuration.alwaysVisibleVote
                }
            })
        }
    }
}

struct RoomSettingsSheet_Previews: PreviewProvider {
    static var previews: some View {
        NavigationWrapper{
            VStack{
                RoomSettingsSheet(object: RoomScreenObject(controller: MockRoomControllerInterfrace()))
                
            }
        }
    }
}
