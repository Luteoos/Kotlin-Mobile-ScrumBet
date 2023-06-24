//
//  RoomUiState.swift
//  ScrumBet
//
//  Created by Mateusz Lutecki on 17/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import core
import Foundation

enum RoomUiState {
    case Success(data: RoomData)
    case Error(error: String)
    case Loading
}
