package com.mora278.camerax.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(navigate: (String) -> Unit) {
    Column {
        Button(
            onClick = {
                navigate.invoke(Routes.CameraX.id)
            }
        ) {
            Text(text = "Open Camera X")
        }
    }
}