package com.example.canvasdesigns.navigation

sealed class Screen(val route: String) {
    data object MainScreen: Screen("MainScreen")
    data object ClockScreen: Screen("ClockScreen")
    data object WeightPickerScreen: Screen("WeightPickerScreen")
    data object PianoScreen: Screen("PianoScreen")
    data object GenderPickerScreen: Screen("GenderPickerScreen")
}