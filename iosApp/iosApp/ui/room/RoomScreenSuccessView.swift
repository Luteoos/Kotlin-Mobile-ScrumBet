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
    @ObservedObject var object: RoomScreenObject
    @State private var isListSheetVisible = false
    @State private var isStyleSheetVisible = false
    
    var body: some View {
        if case .Success(let data) = object.state {
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
            .sheet(isPresented: $isListSheetVisible) {
                HalfSheet {
                    if case .Success(let data) = object.state{
                        RoomScreenMemberListComponent(object: object, isShowingVotes: data.configuration.alwaysVisibleVote)
                    }else{
                        EmptyView()
                    }
                }
            }
            .sheet(isPresented: $isStyleSheetVisible) {
                HalfSheet{
                    StylePickerSheetView(object: object)
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

struct StylePickerSheetView: View{
    @ObservedObject var object: RoomScreenObject
    
    var body: some View{
        if case .Success(let data) = object.state{
            ScrollView{
                LazyVStack {
                    ForEach(data.configuration.scaleTypeList, id: \.self) { styleText in
                        Button {
                            object.setRoomScale(scale: styleText)
                        } label: {
                            Text(styleText.localizedCapitalized)
                                .frame(maxWidth: .infinity)
                        }
                        .padding(.horizontal, 32)
                        .buttonStyle(.bordered)
                        .if(styleText == data.configuration.scaleType, transform: { view in
                            view.tint(Color.primaryColor)
                        })
                            .tint(Color.secondaryColor)
                    }
                }
            }
            .padding(.top, 32)
        }
    }
}

struct RoomScreenSuccessView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView{
            RoomScreenSuccessView(object: RoomScreenObject(controller: MockRoomControllerInterfrace()))            
        }
    }
}
