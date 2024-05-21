package com.example.canvasdesigns.screens.tic_tac_toe_screen

sealed interface WinningLineType {
    data class Vertical(val index: Int): WinningLineType
    data class Horizontal(val index: Int): WinningLineType
    data class Diagonal(val index: Int): WinningLineType
}