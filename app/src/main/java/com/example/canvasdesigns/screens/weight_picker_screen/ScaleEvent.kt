package com.example.canvasdesigns.screens.weight_picker_screen

import androidx.compose.ui.geometry.Offset

sealed interface ScaleEvent {
    data class DragStarted(val scaleCenter: Offset, val dragStartedPosition: Offset): ScaleEvent
    data object DragEnded: ScaleEvent
    data class AngleChanged(val scaleCenter: Offset, val dragAmountPosition: Offset): ScaleEvent
    data object WeightChanged: ScaleEvent
}