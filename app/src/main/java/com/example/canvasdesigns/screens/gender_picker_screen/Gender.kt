package com.example.canvasdesigns.screens.gender_picker_screen

sealed interface Gender {
    data object Male: Gender
    data object Female: Gender
}