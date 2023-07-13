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
                    if(isSettingsVisible){
                        VStack{
                            RoomSettingsSheet(object: object)
                        }.frame(width: 200)
                    }
                    VStack {
                        let votes = data.voteList.filter { user in
                            user.vote != nil
                        }
                        HStack(alignment: .center){
                            Button{
                                withAnimation {
                                    isSettingsVisible.toggle()
                                }
                            } label: {
                                Text("room_settings_label")
                            }
                            .buttonStyle(.borderless)
                        }
                        
                        Divider()
                        
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
                        
                        if(votes.count == data.voteList.count){
                            ScrollView{
                                VStack{
                                    Text(getVoteAverage(votes))
                                        .font(.largeTitle)
                                        .fontWeight(.bold)
                                    Text("average")
                                        .font(.caption)
                                    HStack{
                                        VStack{
                                            Text(data.voteList.min(by: { first, second in
                                                return Int(first.vote ?? "") ?? 0 < Int(second.vote ?? "") ?? 0
                                            })?.vote ?? " ")
                                                .font(.title)
                                                .foregroundColor(Color.red)
                                            Text("min")
                                                .font(.caption)
                                        }
                                        VStack{
                                            Text(data.voteList.max(by: { first, second in
                                                return Int(first.vote ?? "") ?? 0 < Int(second.vote ?? "") ?? 0
                                            })?.vote ?? " ")
                                                .font(.title)
                                                .foregroundColor(Color.green)
                                            Text("max")
                                                .font(.caption)
                                        }
                                    }
                                }
                            }
                        } else {
                            RoomScreenKeyboardComponent(object: object)
                        }
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
                    VStack {
                        RoomScreenMemberListComponent(object: object, isShowingVotes: data.configuration.alwaysVisibleVote)
                    }
                    .frame(minWidth: 500, minHeight: 300)
                }
                .padding(.all, 16)
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
