package com.example.canvasdesigns.screens.weight_picker_screen

sealed interface ScaleLineType {
    data object SmallLine : ScaleLineType
    data object MediumLine: ScaleLineType
    data object LargeLine: ScaleLineType
}