package com.example.canvasdesigns.screens.piano_screen

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Paint
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.log

@Composable
fun Piano(
    modifier: Modifier = Modifier
) {

    val configuration = LocalConfiguration.current

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

    var whiteKeyDimensions by remember {
        mutableStateOf(Pair(0f, 0f))
    }

    var blackKeyDimensions by remember {
        mutableStateOf(Pair(0f, 0f))
    }

    LaunchedEffect(key1 = dragAmountX) {
        whiteKeys = generateWhiteKeys(
            keyWidth = whiteKeyDimensions.first,
            keyHeight = whiteKeyDimensions.second,
            dragOffsetX = dragAmountX,
            canvasHeight = canvasHeight
        )

        blackKeys = generateBlackKeys(
            keyWidth = blackKeyDimensions.first,
            keyHeight = blackKeyDimensions.second,
            dragOffsetX = dragAmountX,
            canvasHeight = canvasHeight
        )
    }

    Canvas(
        modifier = modifier
            .applyIf(configuration.orientation == ORIENTATION_LANDSCAPE, Modifier.padding(top = 200.dp))
            .pointerInput(Unit) {
            detectDragGestures { change, _ ->

                dragAmountX += change.positionChange().x
                dragAmountX = dragAmountX.coerceIn(
                    minimumValue = if(configuration.orientation == ORIENTATION_PORTRAIT) -2370f else -1220f,
                    maximumValue = 0f
                )
            }
        }
            .pointerInput(Unit) {
                detectTapGestures { tapPosition ->

                    blackKeys.forEachIndexed { index, key ->
                        if(
                            tapPosition.x <= key.right &&
                            tapPosition.x >= key.left &&
                            tapPosition.y <= key.bottom &&
                            tapPosition.y >= key.top
                        ) {
                            return@detectTapGestures
                        }
                    }

                    whiteKeys.forEachIndexed  { index, key ->
                        if(
                            tapPosition.x <= key.right &&
                            tapPosition.x >= key.left &&
                            tapPosition.y <= key.bottom &&
                            tapPosition.y >= key.top
                        ) {
                            return@forEachIndexed
                        }
                    }
                }
            }
    ) {
        val width = size.width
        val height = size.height

        canvasHeight = height
        whiteKeyDimensions = Pair(60.dp.toPx(), 200.dp.toPx() - 200f)
        blackKeyDimensions = Pair(60.dp.toPx(), 200.dp.toPx() - 198f)

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

//        for(i in 0..20) {
//            val topLeft = Offset(
//                x = i * 60.dp.toPx() + dragAmountX,
//                y = height / 2  - 200.dp.toPx() + 200f
//            )
//
//            whiteKeys += Rect(
//                topLeft = topLeft,
//                bottomRight = Offset(
//                    x = topLeft.x + 150f,
//                    y = topLeft.y + 400f
//                )
//            )
//
//            drawRect(
//                topLeft = topLeft,
//                color = Color.White,
//                size = Size(150f, 400f)
//            )
//
//            drawContext.canvas.nativeCanvas.apply {
//                drawText(
//                    i.toString(),
//                    i * 60.dp.toPx() + dragAmountX,
//                    height / 2  - 200.dp.toPx() + 500f,
//                    android.graphics.Paint().apply {
//                        textSize = 20.sp.toPx()
//                    }
//                )
//            }
//        }

//        for(i in 0..19) {
//
//            if(i == 2 || i == 6 || i == 9 || i == 13 || i == 16) {
//                continue
//            }
//
//            val topLeft = Offset(
//                x = i * 60.dp.toPx() + 120f + dragAmountX,
//                y = height / 2  - 200.dp.toPx() + 198f
//            )
//
//            drawRect(
//                topLeft = topLeft,
//                color = Color.Black,
//                size = Size(80f, 220f)
//            )
//        }

        blackKeys.forEach {
            drawRect(
                topLeft = it.topLeft,
                size = it.size,
                color = Color.Black
            )
        }
    }
}

private fun generateWhiteKeys(
    keyWidth: Float,
    keyHeight: Float,
    dragOffsetX: Float,
    canvasHeight: Float
): List<Rect> {

    val whiteKeys = mutableListOf<Rect>()
    whiteKeys.clear()

    for (i in 0..20) {
        val topLeft = Offset(
            x = i * keyWidth + dragOffsetX,
            y = canvasHeight / 2 - keyHeight
        )

        whiteKeys.add(
            Rect(
                topLeft = topLeft,
                bottomRight = Offset(
                    x = topLeft.x + 150f,
                    y = topLeft.y + 400f
                )
            )
        )
    }

    return whiteKeys
}

fun Modifier.applyIf(condition: Boolean, modifier: Modifier): Modifier {
    return if(condition) {
        then(modifier)
    } else {
        this
    }
}
private fun generateBlackKeys(
    keyWidth: Float,
    keyHeight: Float,
    dragOffsetX: Float,
    canvasHeight: Float
): List<Rect> {

    var blackKeys = mutableListOf<Rect>()
    blackKeys.clear()

    for(i in 0..19) {

        if(i == 2 || i == 6 || i == 9 || i == 13 || i == 16) {
            continue
        }

        val topLeft = Offset(
            x = i * keyWidth + 120f + dragOffsetX,
            y = canvasHeight / 2  - keyHeight
        )

        blackKeys.add(
            Rect(
                topLeft = topLeft,
                bottomRight = Offset(
                    x = topLeft.x + 80f,
                    y = topLeft.y + 220f
                )
            )
        )
    }
    return blackKeys
}
@Preview(showBackground = true)
@Composable
private fun PianoPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Piano(modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .align(Alignment.Center))
    }
}