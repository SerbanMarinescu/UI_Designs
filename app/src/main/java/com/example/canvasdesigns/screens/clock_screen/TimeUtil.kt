package com.example.canvasdesigns.screens.clock_screen

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
suspend fun getCurTime(): Flow<CurTime> = flow {
    while (true) {
        val curTime = LocalTime.now()
        val curHour = (curTime.hour % 12) * 5
        val curMinute = curTime.minute
        val curSecond = curTime.second

        emit(
            CurTime(
                hour = curHour.toFloat(),
                minute = curMinute.toFloat(),
                second = curSecond.toFloat()
            )
        )
        delay(1000L)
    }
}