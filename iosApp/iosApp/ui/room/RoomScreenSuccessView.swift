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
                    RoomScreenVoteResultComponent(votes: data.voteList, scale: data.configuration.scale)
                } else {
                    RoomScreenKeyboardComponent(object: object)
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
                    isVoteResultVisible = withAnimation(.spring()){
                        data.voteList.filter { user in
                            user.vote != nil
                        }.count == data.voteList.count
                    }
                }
            }
            .toolbar {
                ToolbarItemGroup(placement: .bottomBar) {
                
                    Button {
                        isSettingsSheetVisible.toggle()
                    } label: {
                        Image(systemName: "gear")
                    }
                    Spacer()
                    Button {
                        object.reset()
                    } label: {
                        Image(systemName: "arrow.clockwise")
                    }
                    .disabled(!isOwner)
                    Spacer()
                    Button {
                        isListSheetVisible.toggle()
                    } label: {
                        Image(systemName: "list.bullet")
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
