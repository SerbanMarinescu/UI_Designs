package com.example.canvasdesigns.screens.piano_screen

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canvasdesigns.R
import kotlinx.coroutines.delay
import kotlin.math.log

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Piano(
    modifier: Modifier = Modifier,
) {

    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    val state by remember {
        mutableStateOf(PianoState())
    }

    var dragAmountX by remember {
        mutableFloatStateOf(0f)
    }

    var whiteKeys by remember {
        mutableStateOf(emptyList<Rect>())
    }

    var blackKeys by remember {
        mutableStateOf(emptyList<Rect>())
    }

    var canvasHeight by remember {
        mutableFloatStateOf(0f)
    }

    var whiteKeyOffset by remember {
        mutableStateOf(Pair(0f, 0f))
    }

    var blackKeyOffset by remember {
        mutableStateOf(Pair(0f, 0f))
    }

    var keyPressed by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = dragAmountX) {
        whiteKeys = generateWhiteKeys(
            keyPositionX = whiteKeyOffset.first,
            keyPositionY = whiteKeyOffset.second,
            dragOffsetX = dragAmountX,
            canvasHeight = canvasHeight,
            keyWidth = state.whiteKeyWidth,
            keyHeight = state.whiteKeyHeight
        )

        blackKeys = generateBlackKeys(
            keyPositionX = blackKeyOffset.first,
            keyPositionY = blackKeyOffset.second,
            dragOffsetX = dragAmountX,
            canvasHeight = canvasHeight,
            keyWidth = state.blackKeyWidth,
            keyHeight = state.blackKeyHeight
        )
    }

    Canvas(
        modifier = modifier
            .applyIf(
                configuration.orientation == ORIENTATION_LANDSCAPE,
                Modifier.padding(top = 200.dp)
            )
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->

                    dragAmountX += change.positionChange().x
                    dragAmountX = dragAmountX.coerceIn(
                        minimumValue = if (configuration.orientation == ORIENTATION_PORTRAIT) -2400f else -1410f,
                        maximumValue = 0f
                    )
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { tapPosition ->

                    blackKeys.forEachIndexed { index, key ->
                        if (
                            tapPosition.x <= key.right &&
                            tapPosition.x >= key.left &&
                            tapPosition.y <= key.bottom &&
                            tapPosition.y >= key.top
                        ) {
                            val noteId = mapBlackKeyToNote(index)
                            val mediaPlayer = MediaPlayer.create(context, noteId)
                            mediaPlayer.setOnCompletionListener {
                                mediaPlayer.release()
                            }
                            mediaPlayer.start()
                            return@detectTapGestures
                        }
                    }

                    whiteKeys.forEachIndexed { index, key ->
                        if (
                            tapPosition.x <= key.right &&
                            tapPosition.x >= key.left &&
                            tapPosition.y <= key.bottom &&
                            tapPosition.y >= key.top
                        ) {
                            val noteId = mapWhiteKeyToNote(index)
                            val mediaPlayer = MediaPlayer.create(context, noteId)
                            mediaPlayer.setOnCompletionListener {
                                mediaPlayer.release()
                            }
                            mediaPlayer.start()
                            return@forEachIndexed
                        }
                    }
                }
            }
    ) {
        val width = size.width
        val height = size.height

        canvasHeight = height
        whiteKeyOffset = Pair(60.dp.toPx(), 200.dp.toPx() - 200f)
        blackKeyOffset = Pair(60.dp.toPx(), 200.dp.toPx() - 198f)

        drawRect(
            topLeft = Offset(
                x = 0f,
                y = height / 2 - 200.dp.toPx()
            ),
            color = Color.Gray,
            size = Size(
                width = width,
                height = if(configuration.orientation == ORIENTATION_PORTRAIT) height / 2 - 80f else height / 2 + 410f
            )
        )

        drawRect(
            topLeft = Offset(
                x = 0f,
                y = height / 2 - 200.dp.toPx()
            ),
            color = Color.DarkGray,
            size = Size(width, 200f)
        )

        whiteKeys.forEach {
            drawRect(
                topLeft = it.topLeft,
                size = it.size,
                color = Color.White
            )
        }

        blackKeys.forEach {
            drawRect(
                topLeft = it.topLeft,
                size = it.size,
                color = Color.Black
            )
        }

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "C4",
                7 * whiteKeyOffset.first + 30f + dragAmountX,
                canvasHeight / 2 + 30f,
                Paint().apply {
                    textSize = 15.sp.toPx()
                }
            )
        }
    }
}


