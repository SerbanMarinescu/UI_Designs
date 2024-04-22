package com.example.canvasdesigns.screens.clock_screen

sealed interface ClockLineType {
    data object SmallLine: ClockLineType
    data object BigLine: ClockLineType
}