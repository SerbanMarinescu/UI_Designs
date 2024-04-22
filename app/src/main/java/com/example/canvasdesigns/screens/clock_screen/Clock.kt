package com.example.canvasdesigns.screens.clock_screen

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Clock(
    modifier: Modifier = Modifier,
    curTime: CurTime
) {

    Canvas(modifier = modifier) {

        val radius = 150.dp.toPx()
        val clockWidth = 60.dp.toPx()

        drawCircle(
            center = center,
            radius = radius,
            color = Color.LightGray,
            style = Stroke(width = clockWidth)
        )

        for(i in 0..59) {
            val angleInRad = (i * (360f / 60) - 90) * (PI / 180f).toFloat()

            val lineType = when {
                i % 5 == 0 -> ClockLineType.BigLine
                else -> ClockLineType.SmallLine
            }

            val lineLength = when(lineType) {
                ClockLineType.BigLine -> 20.dp.toPx()
                ClockLineType.SmallLine -> 10.dp.toPx()
            }

            val lineStart = Offset(
                x = center.x + (radius + clockWidth / 2f) * cos(angleInRad),
                y = center.y + (radius + clockWidth / 2f) * sin(angleInRad)
            )

            val lineEnd = Offset(
                x = center.x + (radius + clockWidth / 2f - lineLength) * cos(angleInRad),
                y = center.y + (radius + clockWidth / 2f - lineLength) * sin(angleInRad)
            )

            drawLine(
                start = lineStart,
                end = lineEnd,
                color = Color.Black,
                strokeWidth = 3.dp.toPx()
            )

            drawContext.canvas.nativeCanvas.apply {
                if(lineType is ClockLineType.BigLine) {
                    val textDistance = (radius + clockWidth / 2f - lineLength - 1.dp.toPx() - 20.sp.toPx())
                    val x = center.x + textDistance * cos(angleInRad)
                    val y = center.y + textDistance * sin(angleInRad)

                    drawText(
                        if(i == 0) "12" else (i / 5).toString(),
                        x,
                        y,
                        Paint().apply {
                            textSize = 20.sp.toPx()
                            textAlign = Paint.Align.CENTER
                        }
                    )
                }
            }
        }

        drawCircle(
            color = Color.Black,
            radius = 7.dp.toPx(),
            center = center
        )

        val endMinuteHandPoint = Offset(
            x = center.x + (radius - clockWidth / 2f - 10.dp.toPx()) * cos((curTime.minute * (360f / 60) - 90) * (PI / 180f).toFloat()),
            y = center.y + (radius - clockWidth / 2f - 10.dp.toPx()) * sin((curTime.minute * (360f / 60) - 90) * (PI / 180f).toFloat())
        )

        drawLine(
            start = center,
            end = endMinuteHandPoint,
            color = Color.Black,
            strokeWidth = 4.dp.toPx()
        )

        val endHourHandPoint = Offset(
            x = center.x + (radius - 70.dp.toPx()) * cos((curTime.hour * (360f / 60) - 90) * (PI / 180f).toFloat()),
            y = center.y + (radius - 70.dp.toPx()) * sin((curTime.hour * (360f / 60) - 90) * (PI / 180f).toFloat())
        )

        drawLine(
            start = center,
            end = endHourHandPoint,
            color = Color.Black,
            strokeWidth = 5.dp.toPx()
        )

        val endSecondsHandPoint = Offset(
            x = center.x + (radius - clockWidth / 2f) * cos((curTime.second * (360f / 60) - 90) * (PI / 180f).toFloat()),
            y = center.y + (radius - clockWidth / 2f) * sin((curTime.second * (360f / 60) - 90) * (PI / 180f).toFloat())
        )

        drawLine(
            start = center,
            end = endSecondsHandPoint,
            color = Color.Red,
            strokeWidth = 2.dp.toPx()
        )
    }
}