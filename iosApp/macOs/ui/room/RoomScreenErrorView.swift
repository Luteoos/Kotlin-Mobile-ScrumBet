//
//  RoomScreenErrorView.swift
//  macOs
//
//  Created by Mateusz Lutecki on 22/06/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import SwiftUI

struct RoomScreenErrorView: View {
    var onRetry: () -> ()
    var errorMessage: String
    
    var body: some View {
        VStack{
            Text("error")
                .font(.largeTitle)
            Text(errorMessage)
            Button("retry") {
                onRetry()
            }
        }
    }
}

struct RoomScreenErrorView_Previews: PreviewProvider {
    static var previews: some View {
        VStack{
            RoomScreenErrorView(onRetry: {
                print("retry")
            }, errorMessage: "placeholder error message")            
        }
    }
}
