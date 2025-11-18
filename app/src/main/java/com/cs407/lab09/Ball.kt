package com.cs407.lab09

class Ball(
    private val backgroundWidth: Float,
    private val backgroundHeight: Float,
    private val ballSize: Float
) {
    var posX = 0f
    var posY = 0f
    var velocityX = 0f
    var velocityY = 0f
    private var accX = 0f
    private var accY = 0f

    private var isFirstUpdate = true

    init {
        reset()
    }

    fun updatePositionAndVelocity(xAcc: Float, yAcc: Float, dT: Float) {
        if (isFirstUpdate) {
            isFirstUpdate = false
            accX = xAcc
            accY = yAcc
            return
        }

        val a0x = accX
        val a0y = accY
        val a1x = xAcc
        val a1y = yAcc

        val dt = dT

        // --- Velocity update ---
        // v1 = v0 + 1/2 (a1 + a0) * Δt
        val newVelX = velocityX + 0.5f * (a1x + a0x) * dt
        val newVelY = velocityY + 0.5f * (a1y + a0y) * dt

        // --- Distance update ---
        // l = v0 * Δt + 1/6 (3a0 + a1) * (Δt)^2
        val deltaX = velocityX * dt + (1f / 6f) * (3 * a0x + a1x) * dt * dt
        val deltaY = velocityY * dt + (1f / 6f) * (3 * a0y + a1y) * dt * dt

        posX += deltaX
        posY += deltaY

        velocityX = newVelX
        velocityY = newVelY

        accX = a1x
        accY = a1y

        checkBoundaries()
    }

    fun checkBoundaries() {
        // Left wall
        if (posX < 0f) {
            posX = 0f
            velocityX = 0f
            accX = 0f
        }

        // Right wall
        if (posX + ballSize > backgroundWidth) {
            posX = backgroundWidth - ballSize
            velocityX = 0f
            accX = 0f
        }

        // Top wall
        if (posY < 0f) {
            posY = 0f
            velocityY = 0f
            accY = 0f
        }

        // Bottom wall
        if (posY + ballSize > backgroundHeight) {
            posY = backgroundHeight - ballSize
            velocityY = 0f
            accY = 0f
        }
    }

    fun reset() {
        posX = (backgroundWidth - ballSize) / 2f
        posY = (backgroundHeight - ballSize) / 2f

        velocityX = 0f
        velocityY = 0f
        accX = 0f
        accY = 0f

        isFirstUpdate = true
    }
}