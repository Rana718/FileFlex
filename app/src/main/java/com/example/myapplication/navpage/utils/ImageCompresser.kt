package com.example.myapplication.navpage.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun compressImage(image: Bitmap, targetSizeKB: Int): Bitmap? {
    val targetSizeBytes = targetSizeKB * 1024
    var quality = 100
    var scaledImage = image

    val outputStream = ByteArrayOutputStream()

    if (image.width > 800 || image.height > 800) {
        scaledImage = scaleImage(image, 800)
    }

    while (true) {
        outputStream.reset()
        scaledImage.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        val currentSize = outputStream.size()

        if (currentSize <= targetSizeBytes) {
            break
        }
        quality -= 10

        if (quality < 30) {
            scaledImage = scaleImage(scaledImage, (scaledImage.width * 0.8).toInt())
            quality = 80
        }

        if (quality <= 0) {
            Log.e("ImageCompression", "Failed to compress image under the target size")
            return null
        }
    }

    return BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size())
}

fun saveImage(image: Bitmap, name: String, context: Context) {
    val file = File(context.getExternalFilesDir(null), "${name}_compressed.jpg")
    FileOutputStream(file).use { outputStream ->
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
    }
}

fun calculateImageSize(image: Bitmap?): Int {
    val stream = ByteArrayOutputStream()
    image?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.size() / 1024
}

private fun scaleImage(image: Bitmap, maxSize: Int): Bitmap {
    val aspectRatio = image.width.toFloat() / image.height.toFloat()
    val targetWidth = if (image.width > maxSize) maxSize else image.width
    val targetHeight = (targetWidth / aspectRatio).toInt()
    return Bitmap.createScaledBitmap(image, targetWidth, targetHeight, true)
}
