//
//  MockController.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 24/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import core
import Foundation

class MockController: KControllerInterface {
    func onDeInit() {
        print("deinit")
    }

    func onStart() {
        print("start")
    }

    func onStop() {
        print("stop")
    }

    func watchState() -> CFlow<KState> {
        let mockValue = KStateEmpty()
        return CFlowCompanion().getMock(mockValue: mockValue as KState) as! CFlow<KState>
    }
}
