package com.example.myapplication.navpage.utils

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.foundation.shape.RoundedCornerShape


@Composable
fun FileConverterApp(
    selectedFile: MutableState<Uri?>,
    selectedFileName: MutableState<String>,
    onFileSelect: () -> Unit,
    onFileRemove: () -> Unit,
    onConvertFile: () -> Unit
    ){
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        contentColor = Color.Transparent,
        content = { contentPadding ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Black, Color.DarkGray, Color(0xFF50BDED))
                        )
                    )
                    .padding(contentPadding)

            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select your file",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 20.dp),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                    )
                    Button(
                        onClick = onFileSelect,
                        enabled = selectedFile.value == null
                    ){
                        Text(
                            text = if(selectedFile.value == null) "Select a file" else "File selected",
                        )
                    }

                    if(selectedFile.value != null){
                        Box(
                            modifier = Modifier.padding(16.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 10.dp)
                                    .wrapContentWidth()
                            ) {
                                Text(
                                    text = selectedFileName.value,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                IconButton(
                                    onClick = onFileRemove
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove file",
                                        tint = Color.Red
                                    )
                                }

                            }
                        }
                        Button(onClick = onConvertFile){
                            Text(text = "Convert file")
                        }
                    }
                }
            }
        }
    )
}