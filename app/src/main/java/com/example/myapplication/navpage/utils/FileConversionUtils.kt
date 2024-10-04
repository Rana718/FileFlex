package com.example.myapplication.navpage.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.widget.Toast
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import java.io.*
import java.io.File

fun convertImageToPdf(context: Context, imagePath: String, outputPath: String) {
    val imageFile = File(imagePath)
    if (!imageFile.exists()) {
        Toast.makeText(context, "Image file does not exist!", Toast.LENGTH_SHORT).show()
        return
    }

    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
    val pdfFile = File(outputPath)
    val pdfWriter = PdfWriter(pdfFile.absolutePath)
    val pdfDocument = PdfDocument(pdfWriter)
    val document = Document(pdfDocument)

    try {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()

        val imageData = com.itextpdf.io.image.ImageDataFactory.create(imageBytes)
        val image = Image(imageData)
        document.add(image)

        document.close()

        // Show the PDF save path
        Toast.makeText(context, "PDF saved: ${pdfFile.absolutePath}", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to convert image to PDF: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun convertPdfToImage(context: Context, pdfPath: String, outputImagePath: String, outputFormat: String) {
    val pdfFile = File(pdfPath)
    if (!pdfFile.exists()) {
        Toast.makeText(context, "PDF file does not exist!", Toast.LENGTH_SHORT).show()
        return
    }

    var parcelFileDescriptor: ParcelFileDescriptor? = null
    var pdfRenderer: PdfRenderer? = null
    try {
        parcelFileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
        pdfRenderer = PdfRenderer(parcelFileDescriptor)

        for (pageIndex in 0 until pdfRenderer.pageCount) {
            val page = pdfRenderer.openPage(pageIndex)

            // Create bitmap to render the page, using the page width and height directly
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            // Create output file for each page
            val pageOutputFile = File(outputImagePath.replace(".${outputFormat}", "_page_$pageIndex.${outputFormat}"))
            saveBitmapAsImage(bitmap, pageOutputFile, outputFormat)

            // Show the save path for each converted image
            Toast.makeText(context, "Image saved: ${pageOutputFile.absolutePath}", Toast.LENGTH_SHORT).show()

            // Clean up
            page.close()
            bitmap.recycle()  // Recycle the bitmap to free up memory
        }

        Toast.makeText(context, "PDF converted to images!", Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        Toast.makeText(context, "Error converting PDF to Image: ${e.message}", Toast.LENGTH_SHORT).show()
    } finally {
        pdfRenderer?.close()
        parcelFileDescriptor?.close()
    }
}


// Save Bitmap as Image
fun saveBitmapAsImage(bitmap: Bitmap, outputFile: File, format: String) {
    val outputStream = FileOutputStream(outputFile)
    val compressedFormat = when (format) {
        "jpg" -> Bitmap.CompressFormat.JPEG
        "png" -> Bitmap.CompressFormat.PNG
        else -> return // Unsupported format
    }

    bitmap.compress(compressedFormat, 100, outputStream)
    outputStream.flush()
    outputStream.close()
}