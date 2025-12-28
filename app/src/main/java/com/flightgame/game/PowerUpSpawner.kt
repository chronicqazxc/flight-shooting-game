package com.flightgame.game

import androidx.compose.ui.geometry.Offset
import com.flightgame.model.GameStateHolder
import com.flightgame.model.PowerUp
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Spawns power-ups at random intervals.
 */
class PowerUpSpawner(private val holder: GameStateHolder) {
    private val rnd = Random.Default
    suspend fun start() {
        while (holder.state.value.isRunning) {
            delay(10000) // every 10s
            val newPowerUp = PowerUp(
                pos = Offset(rnd.nextInt(0, 400).toFloat(), -50f)
            )
            holder.update { copy(powerUps = powerUps + newPowerUp) }
        }
    }
}
