//
//  RoomScreenShareSheet.swift
//  macOs
//
//  Created by Mateusz Lutecki on 22/06/2023.
//  Copyright © 2023 luteoos.dev. All rights reserved.
//

import core
import CoreImage.CIFilterBuiltins
import SwiftUI

struct RoomScreenShareSheet: View {
    var url: MultiUrl
    var roomCode: String
    @Environment(\.dismiss) var dismiss

    var body: some View {
        VStack(spacing: 4) {
            Text("share")
                .font(.headline)
                .padding(.top, 16)
            Spacer()
            Image(nsImage: generateQRCode(from: url.appSchema))
                .interpolation(.none)
                .frame(width: 200, height: 200)
                .scaledToFit()
            Spacer()
            Button {
                let pasteboard = NSPasteboard.general
                pasteboard.declareTypes([.string], owner: nil)
                pasteboard.setString(roomCode, forType: .string)
            } label: {
                VStack {
                    Text("room_code")
                    Text(roomCode)
                        .font(.title)
                        .fontWeight(.bold)
                        .tint(Color.primaryColor)
                }
                .flexFrame()
            }
            .padding(.horizontal, 16)
            .buttonStyle(.borderless)
            .tint(Color.primaryColor)
            Spacer()
            Button {
                let pasteboard = NSPasteboard.general
                pasteboard.declareTypes([.string], owner: nil)
                pasteboard.setString(url.httpSchema, forType: .string)
            } label: {
                HStack {
                    Spacer()
                    Text("website_url")
                    Spacer()
                    Image(systemName: "doc.on.doc")
                        .scaledToFit()
                }.frame(maxWidth: .infinity)
            }
            .padding(.horizontal, 16)
            .buttonStyle(.borderedProminent)
            .tint(Color.primaryColor)
            Spacer()
            Button {
                dismiss()
            } label: {
                HStack {
                    Spacer()
                    Text("close")
                    Spacer()
                }.frame(maxWidth: .infinity)
            }
            .padding(.all, 16)
            .buttonStyle(.borderedProminent)
            .tint(Color.secondaryColor)
        }
        .frame(width: 250, height: 500)
    }

    func generateQRCode(from string: String) -> NSImage {
        let context = CIContext()
        let filter = CIFilter.qrCodeGenerator()

        filter.message = Data(string.utf8)

        if let outputImage = filter.outputImage {
            if let cgimg = context.createCGImage(outputImage, from: outputImage.extent) {
                return NSImage(cgImage: cgimg, size: .init(width: 200, height: 200))
            }
        }

        return NSImage()
    }
}

struct RoomScreenShareSheet_Previews: PreviewProvider {
    static var previews: some View {
        RoomScreenShareSheet(url: MultiUrl(base: "placeholder/placeholderRoom"), roomCode: "2137")
    }
}
