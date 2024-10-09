package com.example.myapplication.navpage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import com.example.myapplication.navpage.utils.calculateImageSize
import com.example.myapplication.navpage.utils.compressImage
import com.example.myapplication.navpage.utils.saveImage
import java.io.InputStream

@Composable
fun ImageCom() {
    var originalBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var compressedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var compressedSize by remember { mutableIntStateOf(0) }
    var targetSize by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                originalBitmap = bitmap
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        originalBitmap?.let { bitmap ->
            Text("Original Image")
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Original Image",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                try {
                    val compressed = compressImage(bitmap, targetSize - 25)
                    compressedBitmap = compressed
                    compressedSize = calculateImageSize(compressed)
                    errorMessage = null
                } catch (e: Exception) {
                    errorMessage = "Failed to compress image: ${e.message}"
                }
            }) {
                Text("Compress Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            compressedBitmap?.let { compressed ->
                Text("Compressed Image ($compressedSize KB)")
                Image(
                    bitmap = compressed.asImageBitmap(),
                    contentDescription = "Compressed Image",
                    modifier = Modifier.size(200.dp)
                )
            }

            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { imagePicker.launch("image/*") }) {
            Text("Select Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = targetSize.toString(),
            onValueChange = { newValue ->
                targetSize = newValue.toIntOrNull() ?: 0
            },
            label = { Text("Enter your target size in KB") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            compressedBitmap?.let {
                saveImage(it, name = "origin", context)
            }
        }) {
            Text("Save Compressed Image")
        }
    }

}


