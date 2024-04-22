package com.example.canvasdesigns.screens.weight_picker_screen

data class ScaleState(
    val angle: Float = 0f,
    val dragStartedAngle: Float = 0f,
    val dragEndedAngle: Float = 0f,
    val initialWeight: Int = 50,
    val minWeight: Int = 10,
    val maxWeight: Int = 150,
    val curWeight: Int = initialWeight
)
