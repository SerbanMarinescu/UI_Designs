package com.example.canvasdesigns.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.canvasdesigns.screens.clock_screen.Clock
import com.example.canvasdesigns.screens.clock_screen.ClockViewModel
import com.example.canvasdesigns.screens.main_screen.MainScreen
import com.example.canvasdesigns.screens.main_screen.MainViewModel
import com.example.canvasdesigns.screens.main_screen.NavRouteId
import com.example.canvasdesigns.screens.weight_picker_screen.Scale
import com.example.canvasdesigns.screens.weight_picker_screen.ScaleViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(Screen.MainScreen.route) {

            val viewModel = viewModel<MainViewModel>()
            val state by viewModel.state

            MainScreen(
                itemList = state,
                navigateTo = { id ->
                    when(id) {
                        NavRouteId.CLOCK -> navController.navigate(Screen.ClockScreen.route)
                        NavRouteId.SCALE -> navController.navigate(Screen.WeightPickerScreen.route)
                    }
                }
            )
        }

        composable(Screen.WeightPickerScreen.route) {
            val viewModel = viewModel<ScaleViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Select the weight",
                    fontSize = 30.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 150.dp)
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = 35.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("${state.curWeight} ")
                        }
                        withStyle(
                            SpanStyle(
                                color = Color.DarkGray,
                                fontSize = 20.sp
                            )
                        ) {
                            append("KG")
                        }
                    },
                    modifier = Modifier.align(Alignment.Center)
                )

                Scale(
                    state = state,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }

        composable(Screen.ClockScreen.route) {
            val viewModel = viewModel<ClockViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            Box(modifier = Modifier.fillMaxSize()) {
                Clock(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    curTime = state.curTime
                )
            }
        }
    }
}