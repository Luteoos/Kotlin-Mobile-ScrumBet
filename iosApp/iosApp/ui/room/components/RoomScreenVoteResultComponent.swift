//
//  RoomScreenVoteResult.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 14/07/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import SwiftUI
import Charts
import core

struct RoomScreenVoteResultComponent: View {
    var votes: [RoomUser]
    var scale: [String]
    
    var body: some View {
        ScrollView{
            VStack{
                Text(getVoteAverage(votes))
                    .font(.largeTitle)
                    .fontWeight(.bold)
                Text("average")
                    .font(.caption)
                HStack{
                    VStack{
                        Text(votes.min(by: { first, second in
                            return Int(first.vote ?? "?") ?? 0 < Int(second.vote ?? "?") ?? 0
                        })?.vote ?? " ")
                        .font(.title)
                        .foregroundColor(Color.red)
                        Text("min")
                            .font(.caption)
                    }
                    VStack{
                        Text(votes.max(by: { first, second in
                            return Int(first.vote ?? "?") ?? 0 < Int(second.vote ?? "?") ?? 0
                        })?.vote ?? " ")
                        .font(.title)
                        .foregroundColor(Color.green)
                        Text("max")
                            .font(.caption)
                    }
                }
            }
            .if(votes.count != votes.filter { user in
                user.vote != nil
            }.count) { view in
                withAnimation {
                    view.hidden()
                }
            }
            
            VStack{
                Chart {
                    ForEach(votes, id: \.userId) { item in
                        BarMark(x: .value("vote", item.vote ?? "-"), y: .value("count", 1))
                    }
                }
                .chartXScale(domain: scale)
                .foregroundColor(Color.primaryColor)
            }
            .padding(4)
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

struct RoomScreenVoteResult_Previews: PreviewProvider {
    static var previews: some View {
        @State var votes = [RoomUser(userId: "1", username: "u1", isOwner: true, vote: "2"), RoomUser(userId: "2", username: "u2", isOwner: false, vote: "5")]
        RoomScreenVoteResultComponent(votes: votes, scale: ["2", "5", "6", "."])
    }
}