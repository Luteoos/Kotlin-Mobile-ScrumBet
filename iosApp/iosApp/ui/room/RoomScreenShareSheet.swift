//
//  RoomScreenShareSheet.swift
//  ScrumBet
//
//  Created by Mateusz Lutecki on 18/06/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import core
import CoreImage.CIFilterBuiltins
import SwiftUI

struct RoomScreenShareSheet: View {
    var url: MultiUrl
    var body: some View {
        VStack(spacing: 16) {
            Text("share")
                .font(.headline)
                .padding(.top, 16)
        }

        Spacer()

        Image(uiImage: generateQRCode(from: url.appSchema))
            .interpolation(.none)
            .resizable()
            .scaledToFit()
            .frame(width: 300, height: 300)

        Spacer()

        Button {
            UIPasteboard.general.string = url.appSchema.split(separator: "/").last?.description
        } label: {
            HStack {
                Spacer()
                Text("room_name")
                Spacer()
                Image(systemName: "doc.on.doc")
                    .scaledToFit()
            }.frame(maxWidth: .infinity)
        }
        .padding(.horizontal, 16)
        .tint(Color.secondaryColor)
        .buttonStyle(.borderedProminent)

        Button {
            UIPasteboard.general.string = url.appSchema
        } label: {
            HStack {
                Spacer()
                Text("app_url")
                Spacer()
                Image(systemName: "doc.on.doc")
                    .scaledToFit()
            }.frame(maxWidth: .infinity)
        }
        .padding(.horizontal, 16)
        .buttonStyle(.borderedProminent)
        .tint(Color.secondaryColor)

        Button {
            UIPasteboard.general.string = url.httpSchema
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
        .tint(Color.secondaryColor)
    }

    func generateQRCode(from string: String) -> UIImage {
        let context = CIContext()
        let filter = CIFilter.qrCodeGenerator()

        filter.message = Data(string.utf8)

        if let outputImage = filter.outputImage {
            if let cgimg = context.createCGImage(outputImage, from: outputImage.extent) {
                return UIImage(cgImage: cgimg)
            }
        }

        return UIImage(systemName: "xmark.circle") ?? UIImage()
    }
}

struct RoomScreenShareSheet_Previews: PreviewProvider {
    static var previews: some View {
        HalfSheet {
            RoomScreenShareSheet(url: MultiUrl(base: "placeholder/placeholderPath"))
        }
    }
}
