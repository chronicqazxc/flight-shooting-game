package com.flightgame.model

import androidx.compose.ui.geometry.Offset

/**
 * Projectile fired from aircraft.
 */
data class Projectile(
    var pos: Offset = Offset(0f, 0f),
    var velocity: Offset = Offset(0f, 0f)
)
