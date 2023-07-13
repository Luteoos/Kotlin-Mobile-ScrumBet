//
//  ViewExt.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 24/06/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension View {
    @ViewBuilder
    func `if`<Transform: View>(_ condition: Bool, transform: (Self) -> Transform) -> some View {
        if condition { transform(self) }
        else { self }
    }

    @ViewBuilder
    func roundedMask(cornerRadius: CGFloat = 20) -> some View {
        mask {
            RoundedRectangle(cornerRadius: cornerRadius)
        }
    }
    
    @ViewBuilder
    func flexFrame(_ alignment: Alignment = .center) -> some View{
        frame(
              minWidth: 0,
              maxWidth: .infinity,
              minHeight: 0,
              maxHeight: .infinity,
              alignment: alignment
            )
    }

    #if os(iOS)
        @ViewBuilder
        func card() -> some View {
            frame(maxWidth: .infinity)
                .padding(5)
                .background(Blur(style: .systemUltraThinMaterial))
                .roundedMask()
                .padding(10)
        }
    #endif
}
