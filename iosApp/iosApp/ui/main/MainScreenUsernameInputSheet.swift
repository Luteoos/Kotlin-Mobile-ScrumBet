//
//  MainScreenUsernameInputSheet.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 14/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct MainScreenUsernameInputSheet: View {
    let updateUsername: (String) -> Void
    var username: String
    @Binding var isVisible: Bool

    @State var newUsername = ""

    var body: some View {
        VStack {
            HStack {
                TextField("username", text: $newUsername)
                .textFieldStyle(.roundedBorder)
                .autocorrectionDisabled(true)
                
            }
            .padding(.horizontal, 16)

            Spacer()

            Button {
                updateUsername(newUsername)
                isVisible.toggle()
            } label: {
                Text("save")
                    .frame(maxWidth: .infinity)
            }
            .disabled($newUsername.wrappedValue.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty)
            .padding(.horizontal, 16)
            .buttonStyle(.borderedProminent)
            .tint(Color.secondaryColor)
        }
        .padding(.top, 32)
        .onChange(of: newUsername) { newValue in
            newUsername = newValue.trimmingCharacters(in: .whitespacesAndNewlines)
        }
        .flexFrame()
        
        if UIDevice.current.userInterfaceIdiom == .pad {
            Spacer()
        }
//        .onAppear(perform: {
//            newUsername = username
//        })
    }
}

struct MainScreenUsernameInputSheet_Previews: PreviewProvider {
    static var previews: some View {
        @State var username = "Preview"
        @State var isVisible = true
        MainScreenUsernameInputSheet(
            updateUsername: { name in
                print(name)
                username = name
            }, username: username, isVisible: $isVisible
        )
    }
}
