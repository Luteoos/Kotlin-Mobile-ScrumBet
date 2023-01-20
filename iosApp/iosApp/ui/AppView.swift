//
//  AppView.swift
//  iosApp
//
//  Created by Mateusz Lutecki on 15/06/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct AppView: View {
    @EnvironmentObject var authObject: ObservableObject

    var body: some View {
        Group{
           AnyView(ContentView())
        }
    }
}

struct AppView_Previews: PreviewProvider {
   static var previews: some View {
       AppView().environmentObject(ObservableObject())
   }
}
