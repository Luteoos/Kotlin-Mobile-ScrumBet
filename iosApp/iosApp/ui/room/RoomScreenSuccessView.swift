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
    var data: RoomData
    var currentVote: String?
    var onStyleChange: (String) -> ()
    var onVote: (String) -> ()
    var onReset: () -> ()
    @State var isListSheetVisible = false
    @State var isStyleSheetVisible = false
    
    var body: some View {
            VStack{
                let votes = data.voteList.filter({ user in
                    user.vote != nil
                })
                let isAnyVoteNull = data.voteList.first { user in
                    user.vote == nil
                } != nil
                HStack{
                    Text("\(votes.count)/\(data.voteList.count)")
                    Button("list") {
                        isListSheetVisible.toggle()
                    }
                    .buttonStyle(.bordered)
                    .tint(Color.secondaryColor)
                }
                if(data.configuration.isOwner){
                    Button("choose_style") {
                        isStyleSheetVisible.toggle()
                    }
                    .buttonStyle(.bordered)
                    .tint(Color.secondaryColor)
                }
                Text(getVoteCounter(votes))
                    .font(.title)
                    .if(isAnyVoteNull) { view in
                        view.hidden()
                    }
                if(data.configuration.isOwner){
                    Button("reset") {
                        onReset()
                    }
                    .buttonStyle(.bordered)
                    .tint(Color.secondaryColor)
                }
                ScrollView{
                    LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible()), GridItem(.flexible())]) {
                        ForEach(data.configuration.scale, id: \.self) { text in
                            Button {
                                onVote(text)
                            } label: {
                                ZStack{
                                    if(currentVote == nil || text == currentVote){
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
                        }
                    }
                    .frame(width: 250)
                }
            }
            .sheet(isPresented: $isListSheetVisible) {
                HalfSheet {
                    Text("UserList here")
                }
            }
            .sheet(isPresented: $isStyleSheetVisible) {
                HalfSheet{
                    LazyVStack {
                        Text("Current mode: \(data.configuration.scaleType.localizedCapitalized)")
                        ForEach(data.configuration.scaleTypeList, id: \.self) { styleText in
                            Button(styleText) {
                                onStyleChange(styleText)
                            }
                            .buttonStyle(.bordered)
                        }
                    }
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
        RoomScreenSuccessView(data:MockRoomControllerInterfrace().getMockRoomData(), currentVote: nil, onStyleChange: { text in
            print("style cahnge to \(text)")
        },onVote: { vote in
            print("vote \(vote)")
        }, onReset: {
            print("reset")
        })

//            object: RoomScreenObject(controller: MockRoomControllerInterfrace()))
    }
}
