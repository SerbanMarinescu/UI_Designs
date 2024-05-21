package com.example.canvasdesigns.screens.image_reveal_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.canvasdesigns.R

@Composable
fun ImageReveal() {

    val sakuraTrees = ImageBitmap.imageResource(id = R.drawable.sakura_trees)
    val beach = ImageBitmap.imageResource(id = R.drawable.beach)
    val coloredEye = ImageBitmap.imageResource(id = R.drawable.colored_eye)
    val butterflies = ImageBitmap.imageResource(id = R.drawable.butterflies)
    val starryNight = ImageBitmap.imageResource(id = R.drawable.starry_night)
    val flowerPlanet = ImageBitmap.imageResource(id = R.drawable.flower_planet)
    val colorsImage = ImageBitmap.imageResource(id = R.drawable.colors)
    val woodsCabin = ImageBitmap.imageResource(id = R.drawable.woods_cabin)

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    var dragStarted by remember {
        mutableStateOf(false)
    }

    val imageList = remember {
        listOf(
            sakuraTrees, beach, coloredEye,
            butterflies, starryNight, colorsImage ,
            flowerPlanet, woodsCabin
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectDragGestures(
                        onDragStart = {
                            circleCenter = it
                            dragStarted = true
                        }
                    ) { _, dragAmount ->
                        circleCenter = Offset(
                            x = circleCenter.x + dragAmount.x,
                            y = circleCenter.y + dragAmount.y
                        )
                    }
                }
        ) {

            for(i in imageList.indices) {
                val row = i / 2
                val col = i % 2

                drawImage(
                    image = imageList[i],
                    dstOffset = IntOffset(
                        x = col * size.width.toInt() / 2,
                        y = row * size.height.toInt() / 4
                    ),
                    dstSize = IntSize(
                        width = size.width.toInt() / 2,
                        height = size.height.toInt() / 4
                    ),
                    colorFilter = ColorFilter.tint(Color.Black, BlendMode.Color)
                )
            }

            if(dragStarted) {
                val circlePath = Path().apply {
                    addOval(
                        oval = Rect(
                            center = circleCenter,
                            radius = 300f
                        )
                    )
                }

                clipPath(
                    path = circlePath
                ) {
                    for(i in imageList.indices) {
                        val row = i / 2
                        val col = i % 2

                        drawImage(
                            image = imageList[i],
                            dstOffset = IntOffset(
                                x = col * size.width.toInt() / 2,
                                y = row * size.height.toInt() / 4
                            ),
                            dstSize = IntSize(
                                width = size.width.toInt() / 2,
                                height = size.height.toInt() / 4
                            )
                        )
                    }
                }
            }
        }
    }
}