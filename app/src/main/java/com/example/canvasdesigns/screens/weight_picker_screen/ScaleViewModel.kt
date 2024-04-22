package com.example.canvasdesigns.screens.weight_picker_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

class ScaleViewModel : ViewModel() {

    private val _state = MutableStateFlow(ScaleState())
    val state = _state.asStateFlow()

    fun onEvent(event: ScaleEvent) {
        when(event) {
            ScaleEvent.DragEnded -> {
                _state.update {
                    it.copy(
                        dragEndedAngle = it.angle
                    )
                }
            }
            is ScaleEvent.DragStarted -> {
                _state.update {
                    it.copy(
                        dragStartedAngle = -atan2(
                            event.scaleCenter.x - event.dragStartedPosition.x,
                            event.scaleCenter.y - event.dragStartedPosition.y
                        ) * (180f / PI.toFloat())
                    )
                }
            }

            is ScaleEvent.AngleChanged -> {
                val touchAngle = -atan2(
                    event.scaleCenter.x - event.dragAmountPosition.x,
                    event.scaleCenter.y - event.dragAmountPosition.y
                ) * (180f / PI.toFloat())

                val newAngle = state.value.dragEndedAngle + (touchAngle - state.value.dragStartedAngle)

                _state.update {
                    it.copy(
                        angle = newAngle.coerceIn(
                            minimumValue = state.value.initialWeight - state.value.maxWeight.toFloat(),
                            maximumValue = state.value.initialWeight - state.value.minWeight.toFloat()
                        )
                    )
                }
            }

            ScaleEvent.WeightChanged -> {
                _state.update {
                    it.copy(
                        curWeight = (it.initialWeight - it.angle).roundToInt()
                    )
                }
            }
        }
    }
}