//
//  RoomScreenSuccessView.swift
//  ScrumBet
//
//  Created by Mateusz Lutecki on 18/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import core
import SwiftUI

struct RoomScreenSuccessView: View {
    @ObservedObject var object: RoomScreenObject
    
    @State private var isVoteVisible = false
    @State private var isVoteResultVisible = false
    @State private var isOwner = false
    @State private var voteEnded = false
    @State private var isListSheetVisible = false
    @State private var isSettingsSheetVisible = false

    var body: some View {
        if case let .Success(data) = object.state {
            VStack {
                let votes = data.voteList.filter { user in
                    user.vote != nil
                }
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
                if(isVoteResultVisible){
                    RoomScreenVoteResultComponent(votes: data.voteList, scale: data.configuration.scale, autoRevealVotes:
                        data.configuration.autoRevealVotes)
                } else {
                    RoomScreenKeyboardComponent(object: object)
                }
            }
            .toolbar {
                ToolbarItem(id: UUID().uuidString, placement: .bottomBar, showsByDefault: true) {
                        HStack{
                            Button {
                                isSettingsSheetVisible.toggle()
                            } label: {
                                Image(systemName: "gear")
                            }
                            Spacer()
                            HStack{
                                if(voteEnded){
                                    Button {
                                        object.reset()
                                    } label: {
                                        Image(systemName: "arrow.clockwise")
                                    }
                                }else{
                                    Button {
                                        object.endVote()
                                    } label: {
                                        Image(systemName: "forward.end")
                                    }
                                }
                            }
                            Spacer()
                            Button {
                                isListSheetVisible.toggle()
                            } label: {
                                Image(systemName: "list.bullet")
                            }
                        }
                    }
                }
            .sheet(isPresented: $isListSheetVisible) {
                HalfSheet {
                    RoomScreenMemberListComponent(object: object, isShowingVotes: isVoteVisible)
                }
            }
            .sheet(isPresented: $isSettingsSheetVisible) {
                HalfSheet {
                    RoomSettingsSheet(object: object)
                }
            }
            .onReceive(object.$state) { state in
                if case let .Success(data) = state {
                    isVoteVisible = data.configuration.alwaysVisibleVote
                    isOwner = data.configuration.isOwner
                    voteEnded = data.configuration.voteEnded
                    isVoteResultVisible = withAnimation(.spring()){
                        data.configuration.voteEnded
                    }
                }
            }
            .tint(Color.primaryColor)
        }
    }
}

struct RoomScreenSuccessView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            RoomScreenSuccessView(object: RoomScreenObject(controller: MockRoomControllerInterfrace()))
        }
    }
}
