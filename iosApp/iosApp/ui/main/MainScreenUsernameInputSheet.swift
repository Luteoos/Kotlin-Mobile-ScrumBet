//
//  MainScreenUsernameInputSheet.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 14/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct MainScreenUsernameInputSheet: View {
    let updateUsername: (String) -> ()
    var username: String
    @Binding var isVisible: Bool
    
    @State var newUsername = ""
    
    var body: some View {
//        ZStack{
//            Color.black
            
        VStack{
            HStack{
                Text("username")
                TextField("username", text: $newUsername)
                    .textFieldStyle(.roundedBorder)
            }
//                .background(Color.black)
            .padding(.horizontal, 16)
            
            Spacer()
            
            Button{
                updateUsername(newUsername)
                isVisible.toggle()
            } label: {
                Text("save")
                    .frame(maxWidth: .infinity)
            }
            .padding(.horizontal, 16)
//                .padding(.top, 64)
            .buttonStyle(.borderedProminent)
            .tint(Color.secondaryColor)
        }
        .padding(.top, 32)
        .onAppear(perform: {
            newUsername = username
        })
    }
//    }
}

struct MainScreenUsernameInputSheet_Previews: PreviewProvider {
    static var previews: some View {
        @State var username = "Preview"
        @State var isVisible = true
        MainScreenUsernameInputSheet(
            updateUsername: { name in
                print(name)
                username = name
            }, username: username, isVisible: $isVisible)
    }
}