package com.example.canvasdesigns.screens.tic_tac_toe_screen

import android.text.BoringLayout
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun TicTacToe(
    state: TicTacToeState,
    onEvent: (TicTacToeEvent) -> Unit,
    score: Score
) {

    val scope = rememberCoroutineScope()

    fun CoroutineScope.animateFloatTo1f(animatable: Animatable<Float, AnimationVector1D>) {
        launch {
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(500)
            )
        }
    }

    LaunchedEffect(state.playerWon, state.draw) {
        if(state.playerWon != null) {
            scope.animateFloatTo1f(state.winningLineAnimation)
            onEvent(TicTacToeEvent.PlayerWon)
            delay(3000L)
            onEvent(TicTacToeEvent.Reset)
            return@LaunchedEffect
        }

        if(state.draw) {
            delay(3000L)
            onEvent(TicTacToeEvent.Reset)
            return@LaunchedEffect
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Score",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    onEvent(TicTacToeEvent.ResetScore)
                }) {
                    Text(
                        text = "Reset Score",
                        fontWeight = Bold
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = Bold)) {
                                append("Player X:  ")
                            }
                            withStyle(
                                SpanStyle(
                                    fontWeight = Bold,
                                    color = Color.Red,
                                    fontSize = 18.sp
                                )
                            ) {
                                append(score.playerXScore.toString())
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = Bold)) {
                                append("Player O:  ")
                            }
                            withStyle(
                                SpanStyle(
                                    fontWeight = Bold,
                                    color = Color.Blue,
                                    fontSize = 18.sp
                                )
                            ) {
                                append(score.playerOScore.toString())
                            }
                        }
                    )
                }
            }
        }


        state.playerWon?.let {
            Text(
                text = "Player ${state.playerWon} Won!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
            )
        }

        if(state.draw) {
            Text(
                text = "Draw!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
            )
        }

        Canvas(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center)
                .pointerInput(true) {
                    detectTapGestures { clickOffset ->

                        val position = when {
                            clickOffset.x < size.width / 3f &&
                                    clickOffset.y < size.height / 3f -> {

                                1
                            }

                            clickOffset.x in (size.width / 3f)..(2 * size.width / 3f) &&
                                    clickOffset.y < size.height / 3f -> {

                                2
                            }

                            clickOffset.x > 2 * size.width / 3f &&
                                    clickOffset.y < size.height / 3f -> {

                                3
                            }

                            clickOffset.x < size.width / 3f &&
                                    clickOffset.y in (size.height / 3f)..(2 * size.height / 3f) -> {

                                4
                            }

                            clickOffset.x in (size.width / 3f)..(2 * size.width / 3f) &&
                                    clickOffset.y in (size.height / 3f)..(2 * size.height / 3f) -> {

                                5
                            }

                            clickOffset.x > 2 * size.width / 3f &&
                                    clickOffset.y in (size.height / 3f)..(2 * size.height / 3f) -> {

                                6
                            }

                            clickOffset.x < size.width / 3f &&
                                    clickOffset.y > 2 * size.height / 3f -> {

                                7
                            }

                            clickOffset.x in (size.width / 3f)..(2 * size.width / 3f) &&
                                    clickOffset.y > 2 * size.height / 3f -> {

                                8
                            }

                            clickOffset.x > 2 * size.width / 3f &&
                                    clickOffset.y > 2 * size.height / 3f -> {

                                9
                            }

                            else -> return@detectTapGestures
                        }

                        onEvent(TicTacToeEvent.PlayerMove(position = position))
                        state.playerAnimations[position]?.let { scope.animateFloatTo1f(it) }
                    }
                }
        ) {

            drawLine(
                color = Color.Black,
                start = Offset(
                    x = size.width / 3,
                    y = 0f
                ),
                end = Offset(
                    x = size.width / 3,
                    y = size.height
                ),
                strokeWidth = 5.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = Color.Black,
                start = Offset(
                    x = 2 * size.width / 3,
                    y = 0f
                ),
                end = Offset(
                    x = 2 * size.width / 3,
                    y = size.height
                ),
                strokeWidth = 5.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = Color.Black,
                start = Offset(
                    x = 0f,
                    y = size.height / 3
                ),
                end = Offset(
                    x = size.width,
                    y = size.height / 3
                ),
                strokeWidth = 5.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = Color.Black,
                start = Offset(
                    x = 0f,
                    y = 2 * size.height / 3
                ),
                end = Offset(
                    x = size.width,
                    y = 2 * size.height / 3
                ),
                strokeWidth = 5.dp.toPx(),
                cap = StrokeCap.Round
            )

            state.playerPositions.forEach {

                val (i, j) = when(it.key) {
                    1 -> 0 to 0
                    2 -> 0 to 1
                    3 -> 0 to 2
                    4 -> 1 to 0
                    5 -> 1 to 1
                    6 -> 1 to 2
                    7 -> 2 to 0
                    8 -> 2 to 1
                    9 -> 2 to 2
                    else -> return@forEach
                }

                when(it.value) {
                    Player.O -> {
                        drawArc(
                            color = Color.Blue,
                            startAngle = -100f,
                            sweepAngle = (state.playerAnimations[it.key]?.value?.times(360f)) ?: 360f,
                            useCenter = false,
                            size = Size(200f, 200f),
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round
                            ),
                            topLeft = Offset(
                                x = j * size.width / 3 + 35f,
                                y = i  * size.height / 3 + 35f
                            )
                        )
                    }
                    Player.X -> {
                        val xPath1 = Path().apply {
                            moveTo(
                                x = j * size.width / 3 + 50f,
                                y = i * size.height / 3 + 50f
                            )

                            lineTo(
                                x = (j + 1) * size.width / 3 - 50f,
                                y = (i + 1) * size.height / 3 - 50f
                            )
                        }

                        val xPath2 = Path().apply {
                            moveTo(
                                x = (j + 1) * size.width / 3 - 50f,
                                y = (i) * size.height / 3 + 50f
                            )

                            lineTo(
                                x = (j)  * size.width / 3 + 50f,
                                y = (i + 1) * size.height / 3 - 50f
                            )
                        }

                        val outPath1 = Path()
                        PathMeasure().apply {
                            setPath(xPath1, false)
                            getSegment(0f, (state.playerAnimations[it.key]?.value ?: 1f) * length, outPath1)
                        }

                        val outPath2 = Path()
                        PathMeasure().apply {
                            setPath(xPath2, false)
                            getSegment(0f, (state.playerAnimations[it.key]?.value ?: 1f) * length, outPath2)
                        }

                        drawPath(
                            path = outPath1,
                            color = Color.Red,
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )
                        drawPath(
                            path = outPath2,
                            color = Color.Red,
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )
                    }
                }
            }

            state.winningLine?.let {
                val lineStart = Offset(
                    x = 0f,
                    y = 0f
                )
                val lineEnd = Offset(
                    x = 0f,
                    y = size.height
                )

                when(it) {
                    is WinningLineType.Diagonal -> {
                        when(it.index) {
                            1 -> {
                                rotate(-45f, lineStart) {
                                    val path = Path().apply {
                                        moveTo(lineStart.x, lineStart.y)
                                        lineTo(lineEnd.x, lineEnd.y + 300f)
                                    }

                                    val animatedPath = Path()
                                    PathMeasure().apply {
                                        setPath(path, false)
                                        getSegment(0f, (state.winningLineAnimation.value) * length, animatedPath)
                                    }

                                    drawPath(
                                        path = animatedPath,
                                        color = Color.Black,
                                        style = Stroke(
                                            width = 10.dp.toPx(),
                                            cap = StrokeCap.Round
                                        )
                                    )
                                }
                            }
                            2 -> {
                                translate(
                                    left = size.width
                                ) {
                                    rotate(45f, lineStart) {
                                        val path = Path().apply {
                                            moveTo(lineStart.x, lineStart.y)
                                            lineTo(lineEnd.x, lineEnd.y + 300f)
                                        }

                                        val animatedPath = Path()
                                        PathMeasure().apply {
                                            setPath(path, false)
                                            getSegment(0f, (state.winningLineAnimation.value) * length, animatedPath)
                                        }

                                        drawPath(
                                            path = animatedPath,
                                            color = Color.Black,
                                            style = Stroke(
                                                width = 10.dp.toPx(),
                                                cap = StrokeCap.Round
                                            )
                                        )
                                    }
                                }
                            }
                        }

                    }
                    is WinningLineType.Horizontal -> {
                        translate(
                            top = it.index * size.height / 6
                        ) {
                            rotate(-90f, lineStart) {
                                val path = Path().apply {
                                    moveTo(lineStart.x, lineStart.y)
                                    lineTo(lineEnd.x, lineEnd.y)
                                }

                                val animatedPath = Path()
                                PathMeasure().apply {
                                    setPath(path, false)
                                    getSegment(0f, (state.winningLineAnimation.value) * length, animatedPath)
                                }

                                drawPath(
                                    path = animatedPath,
                                    color = Color.Black,
                                    style = Stroke(
                                        width = 10.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                )
                            }

                        }
                    }
                    is WinningLineType.Vertical -> {
                        translate(
                            left = it.index * size.width / 6
                        ) {
                            val path = Path().apply {
                                moveTo(lineStart.x, lineStart.y)
                                lineTo(lineEnd.x, lineEnd.y)
                            }

                            val animatedPath = Path()
                            PathMeasure().apply {
                                setPath(path, false)
                                getSegment(0f, (state.winningLineAnimation.value) * length, animatedPath)
                            }

                            drawPath(
                                path = animatedPath,
                                color = Color.Black,
                                style = Stroke(
                                    width = 10.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}