package com.example.canvasdesigns.screens.main_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.canvasdesigns.R

class MainViewModel : ViewModel() {

    var state = mutableStateOf(emptyList<DesignThumbnails>())
        private set

    init {
        val itemList = listOf(
            DesignThumbnails(
                resId = R.drawable.scale,
                name = "Scale",
                navRouteId = NavRouteId.SCALE
            ),
            DesignThumbnails(
                resId = R.drawable.clock,
                name = "Clock",
                navRouteId = NavRouteId.CLOCK
            ),
            DesignThumbnails(
                resId = R.drawable.piano,
                name = "Piano",
                navRouteId = NavRouteId.PIANO
            )
        )

        state.value = itemList
    }
}