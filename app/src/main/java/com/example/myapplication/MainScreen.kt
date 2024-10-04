package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.myapplication.navpage.*

@Composable
fun MainScreen(modifier: Modifier = Modifier) {


    val navItems = listOf(
        "PNGCompress" to R.drawable.compress,
        "Converter" to R.drawable.convert,
        "PdfCompress" to R.drawable.pdfcompres
    )

    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.second),
                                contentDescription = item.first
                            )
                        },
                        label = {
                            Text(text = item.first)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    when(selectedIndex){
        0-> ImageCom()
        1-> ConverterScreen()
        2-> PdfCompress()
    }
}
