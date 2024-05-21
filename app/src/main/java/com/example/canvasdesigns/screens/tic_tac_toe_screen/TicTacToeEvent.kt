package com.example.canvasdesigns.screens.tic_tac_toe_screen

sealed interface TicTacToeEvent {
    data class PlayerMove(val position: Int): TicTacToeEvent
    data object PlayerWon: TicTacToeEvent
    data object Reset: TicTacToeEvent
    data object ResetScore: TicTacToeEvent
}