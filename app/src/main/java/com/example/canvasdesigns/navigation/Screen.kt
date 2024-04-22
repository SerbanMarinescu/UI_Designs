package com.example.canvasdesigns.navigation

sealed class Screen(val route: String) {
    data object MainScreen: Screen("MainScreen")
    data object ClockScreen: Screen("ClockScreen")
    data object WeightPickerScreen: Screen("WeightPickerScreen")
}