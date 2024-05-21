package com.example.canvasdesigns.screens.tic_tac_toe_screen

sealed interface Player {
    data object X: Player
    data object O: Player
}