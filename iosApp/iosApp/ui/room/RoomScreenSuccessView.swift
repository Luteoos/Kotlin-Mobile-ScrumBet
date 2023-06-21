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
                        hideSheets()
                        isListSheetVisible.toggle()
                    }
                    .buttonStyle(.bordered)
                    .tint(Color.secondaryColor)
                }
                if(data.configuration.isOwner){
                    Button("choose_style") {
                        hideSheets()
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
                ScrollView{
                    LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible()), GridItem(.flexible())]) {
                        ForEach(data.configuration.scale, id: \.self) { text in
                            Button {
                                object.setVote(vote: text)
                            } label: {
                                ZStack{
                                    if(object.currentVote == nil || text == object.currentVote){
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
                    .padding(.top, 16)
                }
            }
            .sheet(isPresented: $isListSheetVisible) {
                HalfSheet {
                    if case .Success(let data) = object.state{
                        MembersListSheetView(object: object, isShowingVotes: data.configuration.alwaysVisibleVote)
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
    
    func hideSheets(){
        isListSheetVisible = false
        isStyleSheetVisible = false
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

struct MembersListSheetView: View{
    @ObservedObject var object: RoomScreenObject
    @State var isShowingVotes: Bool
    
    var body: some View{
        if case .Success(let data) = object.state{
            VStack{
                Text("member_list")
                    .font(.headline)
                if(data.configuration.isOwner){
                    HStack(){
                        Spacer()
                        Picker("", selection: $isShowingVotes) {
                            Text("show").tag(true)
                            Text("hide").tag(false)
                        }
                        .pickerStyle(.segmented)
                        .frame(maxWidth: 150)
                        .padding(.horizontal, 32)
                        .onChange(of: isShowingVotes, perform: { showVote in
                            object.showVoteVisibility(showVote)
                        })
                    }
                }
                ScrollView{
                    LazyVStack{
                        ForEach(data.voteList.sorted(by: { $0.isOwner && !$1.isOwner}), id: \.userId){ user in
                            HStack{
                                Text(user.username)
                                if(user.isOwner){
                                    Image(systemName: "crown.fill")
                                        .foregroundColor(Color.yellow)
                                        .frame(width: 32, height: 32)
                                }
                                Spacer()
                                ZStack{
                                    if(user.vote != nil){
                                        Color.primaryColor
                                    } else {
                                        Color.secondaryColor
                                    }
                                    if(isShowingVotes){
                                        Text(user.vote ?? " ")
                                    }else{
                                        if(user.vote != nil){
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
            .padding(.vertical, 16)
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
