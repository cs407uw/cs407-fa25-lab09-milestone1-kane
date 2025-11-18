package com.cs407.lab09

import android.hardware.Sensor
import android.hardware.SensorEvent
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BallViewModel : ViewModel() {

    private var ball: Ball? = null
    private var lastTimestamp: Long = 0L

    private val _ballPosition = MutableStateFlow(Offset.Zero)
    val ballPosition: StateFlow<Offset> = _ballPosition.asStateFlow()

    fun initBall(fieldWidth: Float, fieldHeight: Float, ballSizePx: Float) {
        if (ball == null) {
            ball = Ball(
                backgroundWidth = fieldWidth,
                backgroundHeight = fieldHeight,
                ballSize = ballSizePx
            )

            _ballPosition.value = Offset(ball!!.posX, ball!!.posY)
        }
    }

    fun onSensorDataChanged(event: SensorEvent) {
        val currentBall = ball ?: return

        if (event.sensor.type == Sensor.TYPE_GRAVITY) {

            if (lastTimestamp != 0L) {
                val NS2S = 1.0f / 1_000_000_000.0f
                val dT = (event.timestamp - lastTimestamp) * NS2S

                val rawX = event.values[0]
                val rawY = event.values[1]

                // Sensor Y-axis points up. Screen Y-axis points down.
                val xAcc = -rawX
                val yAcc = rawY

                currentBall.updatePositionAndVelocity(
                    xAcc = xAcc,
                    yAcc = yAcc,
                    dT = dT
                )

                _ballPosition.update {
                    Offset(currentBall.posX, currentBall.posY)
                }
            }

            lastTimestamp = event.timestamp
        }
    }

    fun reset() {
        ball?.reset()

        ball?.let {
            _ballPosition.value = Offset(it.posX, it.posY)
        }

        lastTimestamp = 0L
    }
}