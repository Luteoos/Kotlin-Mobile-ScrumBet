//
//  Blur.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 11/08/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

#if os(iOS)
struct Blur: UIViewRepresentable {
    var style: UIBlurEffect.Style = .systemMaterial

    func makeUIView(context: Context) -> UIVisualEffectView {
        return UIVisualEffectView(effect: UIBlurEffect(style: style))
    }

    func updateUIView(_ uiView: UIVisualEffectView, context: Context) {
        uiView.effect = UIBlurEffect(style: style)
    }
}
#else
import AppKit

struct Blur: NSViewRepresentable {
    fileprivate var blending: NSVisualEffectView.BlendingMode
    fileprivate var style: NSVisualEffectView.Material
    
    init(
        blending: NSVisualEffectView.BlendingMode = .behindWindow,
        style: NSVisualEffectView.Material = .fullScreenUI
    ) {
        self.blending = blending
        self.style = style
    }
    
    func makeNSView(context: Context) -> NSVisualEffectView {
        let view = NSVisualEffectView()
        view.blendingMode = blending
        view.material = style
        view.state = .followsWindowActiveState
        return view
    }
    
    func updateNSView(_ nsView: NSVisualEffectView, context: Context) {
        nsView.blendingMode = blending
        nsView.material = style
    }
}
#endif
