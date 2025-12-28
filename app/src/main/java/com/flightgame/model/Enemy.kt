package com.flightgame.model

import androidx.compose.ui.geometry.Offset

/**
 * Enemy aircraft.
 */
data class Enemy(
    var pos: Offset = Offset(0f, 0f),
    var velocity: Offset = Offset(0f, 0f),
    val hp: Int = 1
)
