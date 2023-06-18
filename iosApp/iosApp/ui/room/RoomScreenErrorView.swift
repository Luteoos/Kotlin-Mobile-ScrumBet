//
//  RoomScreenErrorView.swift
//  ScrumBet
//
//  Created by Mateusz Lutecki on 18/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct RoomScreenErrorView: View {
    var onRetry: () -> ()
    var errorMessage: String
    var body: some View {
        VStack(spacing: 16){
            Text("error")
                .font(.headline)
            Text(errorMessage)
                .font(.caption)
            Button("retry") {
                onRetry()
            }
        }
    }
}

struct RoomScreenErrorView_Previews: PreviewProvider {
    static var previews: some View {
        RoomScreenErrorView(onRetry: {
            print("retry")
        }, errorMessage: "plceholderErrorMessage")
    }
}
