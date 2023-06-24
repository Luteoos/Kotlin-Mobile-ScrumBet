//
//  MainScreenJoinSheet.swift
//  ScrumBet
//
//  Created by Mateusz Lutecki on 16/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import CodeScanner
import SwiftUI

struct MainScreenJoinSheet: View {
    let joinRoom: (String) -> Void
    @Binding var isVisible: Bool
    @State var roomName = ""

    var body: some View {
        VStack(spacing: 8) {
            Text("join")
                .font(.headline)

            VStack {
                Text("scan_qr_code")
                    .font(.subheadline)
                CodeScannerView(codeTypes: [.qr], scanMode: .manual, simulatedData: "simulatorPlaceholder") { result in
                    switch result {
                    case let .success(result):
                        roomName = result.string
                    case let .failure(error):
                        print(error)
                    }
                }
            }
            .frame(maxHeight: 400)
            Text("or")
                .font(.subheadline)
            HStack {
                Text("room_name")
                TextField("room_name", text: $roomName)
                    .textFieldStyle(.roundedBorder)
                    .textInputAutocapitalization(.never)
            }
            .padding(.horizontal, 16)

            Spacer()

            Button {
                joinRoom(roomName)
                isVisible.toggle()
            } label: {
                Text("join")
                    .frame(maxWidth: .infinity)
            }
            .disabled(roomName.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty)
            .padding(.horizontal, 16)
            .buttonStyle(.borderedProminent)
            .tint(Color.secondaryColor)
        }
        .padding(.vertical, 16)
        .background(Color.surfaceColor)
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
