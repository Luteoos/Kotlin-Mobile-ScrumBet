//
//  AppKey.swift
//  ScrumBet
//
//  Created by Mateusz Lutecki on 16/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import core
import Foundation
import SwiftUI

private struct AuthKey: EnvironmentKey {
    typealias Value = AuthControllerInterface

    static var defaultValue: Value = MockAuthControllerInterface()
}

extension EnvironmentValues {
    var authController: AuthControllerInterface {
        get { self[AuthKey.self] }
        set { self[AuthKey.self] = newValue }
    }
}
