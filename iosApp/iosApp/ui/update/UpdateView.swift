//
//  UpdateView.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 09/10/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import SwiftUI
import core

struct UpdateView: View {
    var body: some View {
        Button("open_app_store") {
            guard let url = URL(string: PlatformBuildConfig.shared.getAppStoreUrl()) else { return }
            #if os(iOS)
            UIApplication.shared.canOpenURL(url)
            UIApplication.shared.open(url, completionHandler: nil)
            #endif
        }
    }
}

struct UpdateView_Previews: PreviewProvider {
    static var previews: some View {
        UpdateView()
    }
}
