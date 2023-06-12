//
//  AuthUiState.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 12/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

enum AuthUIState{
    case Loading
    case EmptyUserData
    case GeneralError
    case Connected
    case UserSigned
    case InvalidAppVersion
    
}
