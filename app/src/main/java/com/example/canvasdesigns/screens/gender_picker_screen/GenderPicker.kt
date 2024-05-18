package com.example.canvasdesigns.screens.gender_picker_screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.example.canvasdesigns.R
import com.example.canvasdesigns.ui.theme.LightBlue
import com.example.canvasdesigns.ui.theme.Pink

@Composable
fun GenderPicker(
    modifier: Modifier,
    maleColors: List<Color> = listOf(LightBlue, Color.Blue),
    femaleColors: List<Color> = listOf(Pink, Color.Magenta)
) {

    val scaleFactor = 8f
    val distanceBetweenGenders = 100f

    val malePathString = stringResource(id = R.string.male_path)
    val femalePathString = stringResource(id = R.string.female_path)

    val malePath = remember {
        PathParser().parsePathString(malePathString).toPath()
    }
    val femalePath = remember {
        PathParser().parsePathString(femalePathString).toPath()
    }

    val malePathBounds = remember {
        malePath.getBounds()
    }
    val femalePathBounds = remember {
        femalePath.getBounds()
    }

    var transformedMaleOffset by remember {
        mutableStateOf(Offset.Zero)
    }
    var transformedFemaleOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    var currentClickMaleOffset by remember {
        mutableStateOf(Offset.Zero)
    }
    var currentClickFemaleOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    var selectedGender by remember {
        mutableStateOf<Gender>(Gender.Female)
    }

    val maleClickAnimation = animateFloatAsState(
        targetValue = if(selectedGender is Gender.Male) 80f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "Male Click Animation"
    )
    val femaleClickAnimation = animateFloatAsState(
        targetValue = if(selectedGender is Gender.Female) 80f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "Female Click Animation"
    )

    Canvas(
        modifier = modifier.pointerInput(true) {
            detectTapGestures {
                val maleClickRect = Rect(
                    offset = transformedMaleOffset,
                    size = malePathBounds.size * scaleFactor
                )
                val femaleClickRect = Rect(
                    offset = transformedFemaleOffset,
                    size = femalePathBounds.size * scaleFactor
                )

                if(selectedGender !is Gender.Male && maleClickRect.contains(it)) {
                    currentClickMaleOffset = it
                    selectedGender = Gender.Male
                }
                else if(selectedGender !is Gender.Female && femaleClickRect.contains(it)) {
                    currentClickFemaleOffset = it
                    selectedGender = Gender.Female
                }
            }
        }
    ) {

        transformedMaleOffset = Offset(
            x = center.x - malePathBounds.width * scaleFactor - distanceBetweenGenders,
            y = center.y - malePathBounds.height * scaleFactor / 3
        )

        transformedFemaleOffset = Offset(
            x = center.x + distanceBetweenGenders,
            y = center.y - femalePathBounds.height * scaleFactor / 3
        )

        translate(
            left = transformedMaleOffset.x,
            top = transformedMaleOffset.y
        ) {
            scale(
                scale = scaleFactor,
                pivot = malePathBounds.topLeft
            ) {
                drawPath(
                    path = malePath,
                    color = Color.LightGray
                )

                val unTransformedMaleClickOffset = Offset(
                    x = (currentClickMaleOffset.x - transformedMaleOffset.x ) / scaleFactor,
                    y = (currentClickMaleOffset.y - transformedMaleOffset.y ) / scaleFactor
                )

                clipPath(path = malePath) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = maleColors,
                                center = unTransformedMaleClickOffset,
                                radius = maleClickAnimation.value + 1f
                            ),
                            radius = maleClickAnimation.value,
                            center = unTransformedMaleClickOffset
                        )
                }
            }
        }

        translate(
            left = transformedFemaleOffset.x,
            top = transformedFemaleOffset.y
        ) {
            scale(
                scale = scaleFactor,
                pivot = femalePathBounds.topLeft
            ) {
                drawPath(
                    path = femalePath,
                    color = Color.LightGray
                )

                val unTransformedFemaleClickOffset = Offset(
                    x = (currentClickFemaleOffset.x - transformedFemaleOffset.x) / scaleFactor,
                    y = (currentClickFemaleOffset.y - transformedFemaleOffset.y) / scaleFactor
                )

                clipPath(path = femalePath) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = femaleColors,
                                center = if(currentClickFemaleOffset == Offset.Zero) {
                                    femalePathBounds.center
                                } else {
                                    unTransformedFemaleClickOffset
                                },
                                radius = femaleClickAnimation.value + 1f
                            ),
                            radius = femaleClickAnimation.value,
                            center = if(currentClickFemaleOffset == Offset.Zero) {
                                femalePathBounds.center
                            } else {
                                unTransformedFemaleClickOffset
                            }
                        )
                }
            }
        }
    }
}