//
//  RoomScreenMemberListComponent.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 22/06/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import SwiftUI

struct RoomScreenMemberListComponent: View {
    @ObservedObject var object: RoomScreenObject
    @State var isShowingVotes: Bool

    var body: some View {
        if case let .Success(data) = object.state {
            VStack {
                Text("member_list")
                    .font(.headline)
                ScrollView {
                    LazyVStack {
                        ForEach(data.voteList.sorted(by: { $0.isOwner && !$1.isOwner }), id: \.userId) { user in
                            HStack {
                                Text(user.username)
                                if user.isOwner {
                                    Image(systemName: "crown.fill")
                                        .foregroundColor(Color.yellow)
                                        .frame(width: 32, height: 32)
                                }
                                Spacer()
                                ZStack {
                                    if user.vote != nil {
                                        Color.primaryColor
                                    } else {
                                        Color.primaryColor
                                            .opacity(0.4)
                                    }
                                    if isShowingVotes {
                                        Text(user.vote ?? " ")
                                    } else {
                                        if user.vote != nil {
                                            Image(systemName: "checkmark")
                                        }
                                    }
                                }
                                .frame(width: 32, height: 32)
                                .cornerRadius(4)
                            }
                            .padding(.horizontal, 32)
                            Divider()
                        }
                    }
                }
            }
            .onReceive(object.$state, perform: { state in
                if case let .Success(data) = state {
                    isShowingVotes = data.configuration.alwaysVisibleVote
                }
            })
            .padding(.vertical, 16)
        }
    }
}

struct RoomScreenMemberListComponent_Previews: PreviewProvider {
    static var previews: some View {
        RoomScreenMemberListComponent(object: RoomScreenObject(controller: MockRoomControllerInterfrace()), isShowingVotes: false)
    }
}
