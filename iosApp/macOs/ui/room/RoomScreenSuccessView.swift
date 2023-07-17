//
//  RoomScreenSuccessView.swift
//  macOs
//
//  Created by Mateusz Lutecki on 22/06/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import core
import SwiftUI

struct RoomScreenSuccessView: View {
    @ObservedObject var object: RoomScreenObject

    @State var currentStyle = ""
    @State private var isSettingsVisible = false

    var body: some View {
        VStack {
            if case let .Success(data) = object.state {
                HStack {
                    let votes = data.voteList.filter { user in
                        user.vote != nil
                    }
                    
                    VStack {
                        HStack(spacing: 8) {
                            VStack(alignment: .leading){
                                Text("room_members")
                                Text("casted_votes")
                            }
                            VStack{
                                Text("\(data.voteList.count)")
                                    .fontWeight(.bold)
                                Text("\(votes.count)")
                                    .fontWeight(.bold)
                            }
                        }
                        
                        if data.configuration.isOwner {
                            Button {
                                object.reset()
                            } label: {
                                Text("reset")
                                    .frame(width: 220)
                            }
                            .buttonStyle(.bordered)
                            .tint(Color.secondaryColor)
                        }
                        RoomScreenKeyboardComponent(object: object)
                    }
                    .padding(.all, 32)
                    .onChange(of: currentStyle) { scaleStyle in
                        object.setRoomScale(scale: scaleStyle)
                    }
                    .onReceive(object.$state, perform: { state in
                        if case let .Success(data) = state {
                            currentStyle = data.configuration.scaleType
                        }
                    })
                    Divider()
                    VStack{
                        RoomScreenVoteResultComponent(votes: data.voteList, scale: data.configuration.scale)
                    }
                    .frame(minWidth: 300)
                    Divider()
                    VStack {
                        RoomScreenMemberListComponent(object: object, isShowingVotes: data.configuration.alwaysVisibleVote)
                    }
                    .frame(minWidth: 180, minHeight: 500)
                }
                .sheet(isPresented: $isSettingsVisible, content: {
                    VStack{
                        RoomSettingsSheet(object: object)
                        Divider()
                        Button("dismiss") {
                            isSettingsVisible.toggle()
                        }
                    }
                    .padding(8)
                })
                .padding(.all, 16)
                .toolbar {
                    if case let .Success(data) = object.state{
                        ToolbarItem(placement: .secondaryAction) {
                            Button {
                                isSettingsVisible.toggle()
                            } label: {
                                Image(systemName: "gear")
                            }
                            .disabled(!data.configuration.isOwner)
                        }
                    }
                }
            }
        }
    }

    func getVoteAverage(_ votes: [RoomUser]) -> String {
        let list = votes
            .map { user in
                Int(user.vote ?? "?") ?? nil
            }
            .filter { int in
                int != nil
            }
        if list.count != 0 {
            return "\(list.compactMap { $0 }.reduce(0, +) / list.count)"
        } else {
            return " "
        }
    }
}

struct RoomScreenSuccessView_Previews: PreviewProvider {
    static var previews: some View {
        RoomScreenSuccessView(object: RoomScreenObject(controller: MockRoomControllerInterfrace()))
    }
}
