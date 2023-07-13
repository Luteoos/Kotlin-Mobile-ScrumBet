//
//  HalfSheet.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 14/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct HalfSheet<Content>: UIViewControllerRepresentable where Content: View {
    private let content: Content

    @inlinable init(@ViewBuilder content: () -> Content) {
        self.content = content()
    }

    func makeUIViewController(context _: Context) -> HalfSheetController<Content> {
        return HalfSheetController(rootView: content)
    }

    func updateUIViewController(_: HalfSheetController<Content>, context _: Context) {}
}

class HalfSheetController<Content>: UIHostingController<Content> where Content: View {
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)

        if let presentation = sheetPresentationController {
            presentation.detents = [.medium(), .large()]
            presentation.prefersGrabberVisible = true
            presentation.largestUndimmedDetentIdentifier = .none
        }
    }
}
