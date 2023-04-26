//
//  MockController.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 24/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import core
import coreSwift

class MockController : KControllerInterface {
    
    func onDeInit() {
        print("deinit")
    }

    func onStart() {
        print("start")
    }

    func onStop() {
        print("stop")
    }

    func watchState() -> CFlow<KState<AnyObject, AnyObject>> {
        let mockValue = KStateEmpty<AnyObject, AnyObject>()
        return CFlowCompanion().getMock(mockValue: mockValue as KState<AnyObject, AnyObject>) as! CFlow<KState<AnyObject, AnyObject>>
    }
}
