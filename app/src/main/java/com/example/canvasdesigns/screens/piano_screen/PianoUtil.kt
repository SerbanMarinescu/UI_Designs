package com.example.canvasdesigns.screens.piano_screen

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.example.canvasdesigns.R

fun Modifier.applyIf(condition: Boolean, modifier: Modifier): Modifier {
    return if(condition) {
        then(modifier)
    } else {
        this
    }
}

fun generateWhiteKeys(
    keyPositionX: Float,
    keyPositionY: Float,
    dragOffsetX: Float,
    canvasHeight: Float,
    keyWidth: Float,
    keyHeight: Float
): List<Rect> {

    val whiteKeys = mutableListOf<Rect>()
    whiteKeys.clear()

    for (i in 0..20) {
        val topLeft = Offset(
            x = i * keyPositionX + dragOffsetX,
            y = canvasHeight / 2 - keyPositionY
        )

        whiteKeys.add(
            Rect(
                topLeft = topLeft,
                bottomRight = Offset(
                    x = topLeft.x + keyWidth,
                    y = topLeft.y + keyHeight
                )
            )
        )
    }

    return whiteKeys
}


fun generateBlackKeys(
    keyPositionX: Float,
    keyPositionY: Float,
    dragOffsetX: Float,
    canvasHeight: Float,
    keyWidth: Float,
    keyHeight: Float
): List<Rect> {

    val blackKeys = mutableListOf<Rect>()
    blackKeys.clear()

    for(i in 0..19) {

        if(i == 2 || i == 6 || i == 9 || i == 13 || i == 16) {
            continue
        }

        val topLeft = Offset(
            x = i * keyPositionX + 120f + dragOffsetX,
            y = canvasHeight / 2  - keyPositionY
        )

        blackKeys.add(
            Rect(
                topLeft = topLeft,
                bottomRight = Offset(
                    x = topLeft.x + keyWidth,
                    y = topLeft.y + keyHeight
                )
            )
        )
    }
    return blackKeys
}

fun mapWhiteKeyToNote(index: Int): Int {
    return when(index) {
        0 -> R.raw.c_3
        1 -> R.raw.d_3
        2 -> R.raw.e_3
        3 -> R.raw.f_3
        4 -> R.raw.g_3
        5 -> R.raw.a_3
        6 -> R.raw.b_3

        7 -> R.raw.c_4
        8 -> R.raw.d_4
        9 -> R.raw.e_4
        10 -> R.raw.f_4
        11 -> R.raw.g_4
        12 -> R.raw.a_4
        13 -> R.raw.b_4

        14 -> R.raw.c_5
        15 -> R.raw.d_5
        16 -> R.raw.e_5
        17 -> R.raw.f_5
        18 -> R.raw.g_5
        19 -> R.raw.a_5
        20 -> R.raw.b_5
        else -> throw IllegalArgumentException("Invalid Index for Note")
    }
}

fun mapBlackKeyToNote(index: Int): Int {
    return when(index) {
        0 -> R.raw.cs_3
        1 -> R.raw.ds_3
        2 -> R.raw.fs_3
        3 -> R.raw.gs_3
        4 -> R.raw.as_3

        5 -> R.raw.cs_4
        6 -> R.raw.ds_4
        7 -> R.raw.fs_4
        8 -> R.raw.gs_4
        9 -> R.raw.as_4

        10 -> R.raw.cs_5
        11 -> R.raw.ds_5
        12 -> R.raw.fs_5
        13 -> R.raw.gs_5
        14 -> R.raw.as_5
        else -> throw IllegalArgumentException("Invalid Index for Note")
    }
}