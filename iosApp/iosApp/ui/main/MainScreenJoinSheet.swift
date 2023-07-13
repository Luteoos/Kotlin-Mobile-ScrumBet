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
    @State var uiState = JoinSheetUiState.Default

    var body: some View {
        VStack(spacing: 8) {
            switch uiState {
            case .Default:
                Button {
                    uiState = .QR
                } label: {
                    Text("scan_qr_code")
                        .frame(width: 300)
                }
                .buttonStyle(.borderedProminent)
                .tint(Color.primaryColor)
                
                Text("or")
                    .font(.subheadline)
                
                Button{
                    uiState = .NumericCode
                } label: {
                    Text("enter_room_code")
                        .frame(width: 300)
                }
                .buttonStyle(.borderedProminent)
                .tint(Color.primaryColor)
            case .QR:
                CodeScannerView(codeTypes: [.qr], scanMode: .continuous, simulatedData: "simulatorPlaceholder") { result in
                    switch result {
                    case let .success(result):
                        joinRoom(result.string)
                        isVisible.toggle()
                    case let .failure(error):
                        print(error)
                    }
                }
            case .NumericCode:
                HStack {
                    TextField("room_code", text: $roomName)
                        .textFieldStyle(.roundedBorder)
                        .keyboardType(.numberPad)
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
        }
        .flexFrame()
        .padding(.vertical, 16)
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
