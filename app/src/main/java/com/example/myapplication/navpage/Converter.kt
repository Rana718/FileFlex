package com.example.myapplication.navpage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.Modifier
import com.example.myapplication.navpage.utils.FileConverterApp
import com.example.myapplication.navpage.utils.FileConverterViewModel

@Composable
fun ConverterScreen() {
    val viewModel: FileConverterViewModel = viewModel()
    val context = LocalContext.current

    val selectedFile = remember { mutableStateOf<Uri?>(null) }
    val selectedFileName = remember { mutableStateOf("") }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedFile.value = uri
                selectedFileName.value = viewModel.getFileNameFromUri(context.contentResolver, uri)
            }
        }
    }

    fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        filePickerLauncher.launch(intent)
    }


    fun removeSelectedFile() {
        selectedFile.value = null
        selectedFileName.value = ""
    }


    FileConverterApp(
        selectedFile = selectedFile,
        selectedFileName = selectedFileName,
        onFileSelect = { openFilePicker() },
        onFileRemove = { removeSelectedFile() },
        onConvertFile = { selectedFile.value?.let { uri -> viewModel.handleFileSelection(context, uri) } }
    )
}
