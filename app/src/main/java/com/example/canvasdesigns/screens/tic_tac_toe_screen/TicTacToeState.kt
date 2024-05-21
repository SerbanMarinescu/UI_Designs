package com.example.canvasdesigns.screens.tic_tac_toe_screen


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D

data class TicTacToeState(
    val curPlayer: Player? = null,
    val moveCount: Int = 0,
    val playerPositions: Map<Int, Player> = mapOf(),
    val playerAnimations: Map<Int, Animatable<Float, AnimationVector1D>> = mapOf(),
    val draw: Boolean = false,
    val playerWon: Player? = null,
    val winningLine: WinningLineType? = null,
    val winningLineAnimation: Animatable<Float, AnimationVector1D> = Animatable(0f)
)
