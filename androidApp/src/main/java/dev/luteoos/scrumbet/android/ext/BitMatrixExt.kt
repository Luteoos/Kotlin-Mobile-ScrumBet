package dev.luteoos.scrumbet.android.ext

// import com.google.zxing.common.BitMatrix
//
// fun BitMatrix.encodeToBitmap(): Bitmap {
//    val pixels = IntArray(width * height)
//    for (y in 0 until height) {
//        val offset = y * width
//        for (x in 0 until width) {
//            pixels[offset + x] = if (get(x, y)) Color.BLACK else Color.parseColor("#8883D2")
//        }
//    }
//    return Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565).apply {
//        setPixels(pixels, 0, width, 0, 0, width, height)
//    }
// }
