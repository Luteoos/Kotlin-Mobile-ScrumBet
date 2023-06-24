//
//  NavigationWrapper.swift
//  ScrumBetMacOs
//
//  Created by Mateusz Lutecki on 22/06/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import Foundation
import SwiftUI

struct NavigationWrapper<Content>: View where Content: View {
    @ViewBuilder var content: () -> Content

    var body: some View {
//        if #available(iOS 16, *) {
//            NavigationStack(root: content)
//        } else {
        NavigationStack(root: content)
            .tint(Color.secondaryColor)
//        }
    }
}
