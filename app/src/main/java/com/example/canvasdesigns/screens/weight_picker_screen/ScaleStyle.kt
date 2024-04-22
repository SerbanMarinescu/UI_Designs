package com.example.canvasdesigns.screens.weight_picker_screen

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ScaleStyle(
    val scaleWidth: Dp = 150.dp,
    val radius: Dp = 550.dp,
    val smallLineColor: Color = Color.LightGray,
    val mediumLineColor: Color = Color.Green,
    val largeLineColor: Color = Color.Black,
    val smallLineLength: Dp = 15.dp,
    val mediumLineLength: Dp = 25.dp,
    val largeLineLength: Dp = 35.dp,
    val scaleIndicatorColor: Color = Color.Red,
    val scaleIndicatorLength: Dp = 60.dp,
    val textSize: TextUnit = 18.sp
)
