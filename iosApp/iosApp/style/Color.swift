//
//  Color.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 21/06/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI

extension Color{
    static let background : some View = backgroundColor.ignoresSafeArea(.all)//Color.mint.ignoresSafeArea(.all)
    static let backgroundCard: some View = Blur(style: .systemUltraThinMaterial) // Color.white.blendMode(.screen)//opacity(0.15)//.opacity(0.8).blur(radius: 100)//.shadow(radius: 10)
    static let backgroundColor = Color("Background")
    static let primaryColor = Color("Primary")
    static let accentColor = Color("Accent")
    static let errorColor = Color("Error")
}