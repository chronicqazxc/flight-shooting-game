package com.flightgame.model

import androidx.compose.ui.geometry.Offset

/**
 * Simplified aircraft representation.
 * Pos is the center location in screen coordinates.
 * Velocity is the movement per second.
 */
data class Aircraft(
    var pos: Offset = Offset(0f, 0f),
    var velocity: Offset = Offset(0f, 0f),
    val height: Float = 50f,
    val width: Float = 50f,
    val hasShield: Boolean = false
)
