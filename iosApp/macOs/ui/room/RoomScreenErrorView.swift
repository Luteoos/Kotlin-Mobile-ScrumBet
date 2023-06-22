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
        Text("Error")
    }
}

struct RoomScreenErrorView_Previews: PreviewProvider {
    static var previews: some View {
        RoomScreenErrorView(onRetry: {
            print("retry")
        }, errorMessage: "placeholder error message")
    }
}
