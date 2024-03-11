package com.ravenzip.devicepicker.converters

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun byteArrayToImageBitmap(data: ByteArray): ImageBitmap {
    return BitmapFactory.decodeByteArray(data, 0, data.size).asImageBitmap()
}
