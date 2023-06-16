//
//  MainScreenJoinSheet.swift
//  ScrumBet
//
//  Created by Mateusz Lutecki on 16/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import CodeScanner

struct MainScreenJoinSheet: View {
    let joinRoom: (String) -> ()
    @Binding var isVisible: Bool
    @State var roomName = ""
    
    var body: some View {
        VStack(spacing: 8){
            Text("join")
                .font(.caption2)
            
            HStack{
                Text("room_name")
                TextField("room_name", text: $roomName)
                    .textFieldStyle(.roundedBorder)
            }
//                .background(Color.black)
            .padding(.horizontal, 16)
        }
        
        Spacer()
        
        Button{
            joinRoom(roomName)
            isVisible.toggle()
        } label: {
            Text("join")
                .frame(maxWidth: .infinity)
        }
        .padding(.horizontal, 16)
//                .padding(.top, 64)
        .buttonStyle(.borderedProminent)
        .tint(Color.secondary)
    }
}

struct MainScreenJoinSheet_Previews: PreviewProvider {
    static var previews: some View {
        @State var isVisible = true
        
        MainScreenJoinSheet(joinRoom: { roomName in
            print(roomName)
        }, isVisible: $isVisible)
    }
}
