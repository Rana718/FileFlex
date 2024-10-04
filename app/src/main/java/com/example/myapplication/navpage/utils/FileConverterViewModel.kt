package com.example.myapplication.navpage.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FileConverterViewModel: ViewModel() {
    fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String{
        var name = ""
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use{
            if(it.moveToFirst()){
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                name = it.getString(nameIndex)
            }
        }
        return name
    }

    fun handleFileSelection(context: Context, uri: Uri?){
        if (uri == null) return

        val fileName = getFileNameFromUri(context.contentResolver, uri)
        val inputStream = context.contentResolver.openInputStream(uri)
        val inputFile = File(context.cacheDir, fileName)

        inputStream?.let{
            copyStreamToFile(it, inputFile)
            val extension = fileName.substringAfterLast(".").lowercase()
            val outputPath = when(extension){
                "pdf" -> getOutputPath(fileName,"jpg")
                "jpg", "jpeg", "png" -> getOutputPath(fileName,"pdf")
                else -> null
            }

            if(outputPath != null){
                when (extension){
                    "pdf" -> convertPdfToImage(context, inputFile.absolutePath, outputPath, "jpg")
                    "jpg", "jpeg", "png" -> convertImageToPdf(context, inputFile.absolutePath, outputPath)

                }
            }
            else{
                Toast.makeText(context, "Unsupported file format", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun getOutputPath(fileName: String, format: String): String{
        val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "FileConverter")
        if(!folder.exists()){
            folder.mkdirs()
        }
        return File(folder, "converted_$fileName.$format").absolutePath
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File){
        FileOutputStream(outputFile).use{outputStream ->
            val buffer = ByteArray(1024)
            var length: Int
            while(inputStream.read(buffer).also{length = it} > 0){
                outputStream.write(buffer, 0, length)
            }
            inputStream.close()
        }
    }
}