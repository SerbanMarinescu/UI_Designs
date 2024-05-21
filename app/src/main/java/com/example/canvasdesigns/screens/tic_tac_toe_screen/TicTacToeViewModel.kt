package com.example.canvasdesigns.screens.tic_tac_toe_screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicTacToeViewModel : ViewModel() {

    private val _state = MutableStateFlow(TicTacToeState())
    val state = _state.asStateFlow()

    var score by mutableStateOf(Score())
        private set

    private val playerPosition = mutableMapOf<Int, Player>()
    private val playerAnimations = mutableMapOf<Int, Animatable<Float, AnimationVector1D>>()

    init {
        initAnimations()
    }

    fun onEvent(event: TicTacToeEvent) {
        when (event) {
            is TicTacToeEvent.PlayerMove -> {
                if (state.value.moveCount < 10) {
                    if (playerPosition.containsKey(event.position)) {
                        return
                    }
                    if(state.value.playerWon != null || state.value.draw) {
                        return
                    }

                    val nextPlayer = when(state.value.curPlayer) {
                        Player.O -> Player.X
                        Player.X -> Player.O
                        null -> Player.X
                    }

                    playerPosition[event.position] = nextPlayer

                    _state.update {
                        it.copy(
                            curPlayer = nextPlayer,
                            moveCount = it.moveCount + 1,
                            playerPositions = playerPosition
                        )
                    }
                    checkForWinOrDraw()
                }
            }

            TicTacToeEvent.Reset -> {
                playerPosition.clear()
                _state.update {
                    TicTacToeState()
                }
                initAnimations()
            }

            TicTacToeEvent.PlayerWon -> {
                when(state.value.curPlayer) {
                    Player.O -> {
                        score = score.copy(playerOScore = score.playerOScore + 1)
                    }
                    Player.X -> score = score.copy(playerXScore = score.playerXScore + 1)
                    null -> Unit
                }
            }

            TicTacToeEvent.ResetScore -> {
                score = Score()
            }
        }
    }

    private fun checkForWinOrDraw() {

        val horizontalWin = checkWin(1,2,3) ||
                            checkWin(4,5,6) ||
                            checkWin(7,8,9)

        val verticalWin = checkWin(1, 4, 7) ||
                          checkWin(2,5,8) ||
                          checkWin(3,6,9)

        val diagonalWin = checkWin(1,5,9) ||
                          checkWin(3,5,7)

        val winHorizontal = when {
            checkWin(1,2,3) -> WinningLineType.Horizontal(1)
            checkWin(4,5,6) -> WinningLineType.Horizontal(3)
            checkWin(7,8,9) -> WinningLineType.Horizontal(5)
            else -> null
        }
        val winVertical = when {
            checkWin(1, 4, 7) -> WinningLineType.Vertical(1)
            checkWin(2,5,8) -> WinningLineType.Vertical(3)
            checkWin(3,6,9) -> WinningLineType.Vertical(5)
            else -> null
        }
        val winDiagonal = when {
            checkWin(1,5,9) -> WinningLineType.Diagonal(1)
            checkWin(3,5,7) -> WinningLineType.Diagonal(2)
            else -> null
        }

        winHorizontal?.let { line ->
            _state.update {
                it.copy(
                    winningLine = line
                )
            }
        }

        winVertical?.let { line ->
            _state.update {
                it.copy(
                    winningLine = line
                )
            }
        }

        winDiagonal?.let { line ->
            _state.update {
                it.copy(
                    winningLine = line
                )
            }
        }



        if (horizontalWin || verticalWin || diagonalWin) {
            _state.update {
                it.copy(playerWon = it.curPlayer)
            }

            return
        }

        if (state.value.moveCount == 9) {
            _state.update {
                it.copy(draw = true)
            }
        }
    }

    private fun checkWin(pos1: Int, pos2: Int, pos3: Int): Boolean {
        return playerPosition[pos1] != null
                && playerPosition[pos1] == playerPosition[pos2]
                && playerPosition[pos2] == playerPosition[pos3]
    }

    private fun initAnimations() {
        playerAnimations.clear()
        for(i in 1..9) {
            playerAnimations[i] = Animatable(0f)
        }
        _state.update {
            it.copy(playerAnimations = playerAnimations)
        }
    }
}