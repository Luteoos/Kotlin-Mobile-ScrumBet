//
//  RoomScreenSuccessView.swift
//  macOs
//
//  Created by Mateusz Lutecki on 22/06/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import SwiftUI
import core

struct RoomScreenSuccessView: View {
    @ObservedObject var object: RoomScreenObject
    
    @State var currentStyle = ""
    
    var body: some View {
        VStack{
            if case .Success(let data) = object.state {
                HStack{
                    VStack{
                        let votes = data.voteList.filter({ user in
                            user.vote != nil
                        })
                        let isAnyVoteNull = data.voteList.first { user in
                            user.vote == nil
                        } != nil
                        HStack{
                            Text("\(votes.count)/\(data.voteList.count)")
                                .font(.caption2)
                        }
                        Text(getVoteCounter(votes))
                            .font(.title)
                            .if(isAnyVoteNull) { view in
                                view.hidden()
                            }
                        if(data.configuration.isOwner){
                            Picker("choose_style", selection: $currentStyle) {
                                ForEach(data.configuration.scaleTypeList, id: \.self) {
                                    Text($0.localizedCapitalized).tag($0)
                                }
                            }
                            .frame(width: 220)
                        }
                        if(data.configuration.isOwner){
                            Button {
                                object.reset()
                            } label: {
                                Text("reset")
                                    .frame(width: 220)
                            }
                            .buttonStyle(.bordered)
                            .tint(Color.secondaryColor)
                        }
                        Divider()
                        RoomScreenKeyboardComponent(object: object)
                    }
                    .padding(.all, 32)
                    .onChange(of: currentStyle) { scaleStyle in
                        object.setRoomScale(scale: scaleStyle)
                    }
                    .onReceive(object.$state, perform: { state in
                        if case .Success(let data) = state{
                            currentStyle = data.configuration.scaleType
                        }
                    })
                    Divider()
                    VStack{
                        RoomScreenMemberListComponent(object: object, isShowingVotes: data.configuration.alwaysVisibleVote)
                    }
                    .frame(minWidth: 400)
                }
                .padding(.all, 16)
            }
        }
    }
    
    func getVoteCounter(_ votes: [RoomUser]) -> String{
        let list = votes
            .map { user in
                Int(user.vote ?? "?") ?? nil
            }
            .filter { int in
                int != nil
            }
        if list.count != 0{
            return "\(list.compactMap{$0}.reduce(0, +)/list.count)"
        }else{
            return " "
        }
    }
}

struct RoomScreenSuccessView_Previews: PreviewProvider {
    static var previews: some View {
        RoomScreenSuccessView(object: RoomScreenObject(controller: MockRoomControllerInterfrace()))
    }
}
