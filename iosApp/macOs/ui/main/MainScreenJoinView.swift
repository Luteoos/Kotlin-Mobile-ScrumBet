//
//  MainScreenJoinView.swift
//  macOs
//
//  Created by Mateusz Lutecki on 22/06/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import SwiftUI

struct MainScreenJoinView: View {
    var onJoin: (String) -> Void
    @Binding var isVisible: Bool

    @State var roomId: String = ""

    var body: some View {
        VStack {
            Text("join")
                .font(.headline)
            Spacer()
            HStack {
                Text("room_name")
                TextField("room_name", text: $roomId)
            }
            Spacer()
            HStack {
                Button {
                    isVisible.toggle()
                } label: {
                    Text("cancel")
                        .frame(maxWidth: .infinity)
                }
                Button {
                    onJoin(roomId)
                    isVisible.toggle()
                } label: {
                    Text("join")
                        .frame(maxWidth: .infinity)
                }
                .disabled($roomId.wrappedValue.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty)
                .buttonStyle(.borderedProminent)
                .tint(Color.primaryColor)
            }
        }
        .padding(.all, 16)
        .frame(minWidth: 400, minHeight: 200)
    }
}

struct MainScreenJoinView_Previews: PreviewProvider {
    static var previews: some View {
        @State var isVisible = true
        MainScreenJoinView(onJoin: { roomId in
            print("roomId: \(roomId)")
        }, isVisible: $isVisible)
    }
}
