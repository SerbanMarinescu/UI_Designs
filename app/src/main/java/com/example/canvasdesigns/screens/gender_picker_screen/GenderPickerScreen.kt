package com.example.canvasdesigns.screens.gender_picker_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GenderPickerScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Select your gender",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )

        GenderPicker(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        Button(
            onClick = {

            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
        ) {
            Text(
                text = "Next",
                fontSize = 20.sp
            )
        }
    }
}