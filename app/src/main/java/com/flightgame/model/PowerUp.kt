package com.flightgame.model

import androidx.compose.ui.geometry.Offset

data class PowerUp(
    val pos: Offset,
    val velocity: Offset = Offset(0f, 200f), // Move down
    val type: PowerUpType = PowerUpType.SHIELD
)
