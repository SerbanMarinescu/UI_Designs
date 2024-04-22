package com.example.canvasdesigns.screens.weight_picker_screen

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withRotation
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun Scale(
    modifier: Modifier = Modifier,
    style: ScaleStyle = ScaleStyle(),
    state: ScaleState,
    onEvent: (ScaleEvent) -> Unit
) {

    val radius = style.radius
    val scaleWidth = style.scaleWidth

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    Canvas(
        modifier = modifier
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { offset ->
                        onEvent(ScaleEvent.DragStarted(circleCenter, offset))
                    },
                    onDragEnd = {
                        onEvent(ScaleEvent.DragEnded)
                    }
                ) { change, _ ->

                    onEvent(ScaleEvent.AngleChanged(circleCenter, change.position))
                    onEvent(ScaleEvent.WeightChanged)
                }
            }
    ) {
        circleCenter = Offset(
            x = center.x,
            y = scaleWidth.toPx() / 2f + radius.toPx()
        )
        val outerRadius = radius.toPx() + scaleWidth.toPx() / 2f
        val innerRadius = radius.toPx() - scaleWidth.toPx() / 2f

        drawContext.canvas.nativeCanvas.apply {
            drawCircle(
                circleCenter.x,
                circleCenter.y,
                radius.toPx(),
                Paint().apply {
                    strokeWidth = scaleWidth.toPx()
                    color = Color.WHITE
                    setStyle(Paint.Style.STROKE)
                    setShadowLayer(
                        60f,
                        0f,
                        0f,
                        Color.argb(50, 0, 0, 0)
                    )
                }
            )
        }

        for(i in state.minWeight..state.maxWeight) {
            val angleInRad = (i - state.initialWeight + state.angle - 90) * (PI / 180f).toFloat()
            val lineType = when {
                i % 10 == 0 -> ScaleLineType.LargeLine
                i % 5 == 0 -> ScaleLineType.MediumLine
                else -> ScaleLineType.SmallLine
            }
            val lineColor = when(lineType) {
                ScaleLineType.LargeLine -> style.largeLineColor
                ScaleLineType.MediumLine -> style.mediumLineColor
                ScaleLineType.SmallLine -> style.smallLineColor
            }
            val lineLength = when(lineType) {
                ScaleLineType.LargeLine -> style.largeLineLength
                ScaleLineType.MediumLine -> style.mediumLineLength
                ScaleLineType.SmallLine -> style.smallLineLength
            }
            val lineStart = Offset(
                x = (outerRadius - lineLength.toPx()) * cos(angleInRad) + circleCenter.x,
                y = (outerRadius - lineLength.toPx()) * sin(angleInRad) + circleCenter.y
            )
            val lineEnd = Offset(
                x = outerRadius * cos(angleInRad) + circleCenter.x,
                y = outerRadius * sin(angleInRad) + circleCenter.y
            )

            drawLine(
                color = lineColor,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 1.dp.toPx()
            )

            drawContext.canvas.nativeCanvas.apply {
                if(lineType is ScaleLineType.LargeLine) {
                    val textRadius = outerRadius - lineLength.toPx() - 5.dp.toPx() - style.textSize.toPx()
                    val x = textRadius * cos(angleInRad) + circleCenter.x
                    val y = textRadius * sin(angleInRad) + circleCenter.y

                    withRotation(
                        degrees = angleInRad * (180f / PI.toFloat()) + 90,
                        pivotX = x,
                        pivotY = y
                    ) {
                        drawText(
                            abs(i).toString(),
                            x,
                            y,
                            Paint().apply {
                                textSize = style.textSize.toPx()
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    }
                }
            }
        }

        val indicatorTop = Offset(
            x = circleCenter.x,
            y = circleCenter.y - innerRadius - style.scaleIndicatorLength.toPx()
        )
        val indicatorBtmLeft = Offset(
            x = circleCenter.x - 5f,
            y = circleCenter.y - innerRadius
        )
        val indicatorBtmRight = Offset(
            x = circleCenter.x + 5f,
            y = circleCenter.y - innerRadius
        )
        val indicator = Path().apply {
            moveTo(indicatorTop.x, indicatorTop.y)
            lineTo(indicatorBtmLeft.x, indicatorBtmLeft.y)
            lineTo(indicatorBtmRight.x, indicatorBtmRight.y)
            close()
        }

        drawPath(
            path = indicator,
            color = style.scaleIndicatorColor
        )
    }
}