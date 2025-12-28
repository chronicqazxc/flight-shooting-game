package com.flightgame.game

import androidx.compose.ui.geometry.Offset
import com.flightgame.model.Enemy
import com.flightgame.model.GameStateHolder
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Simple enemy spawner coroutine that injects enemies into the GameState in waves.
 */
class EnemySpawner(private val holder: GameStateHolder) {
    private val rnd = Random.Default

    private data class Wave(
        val enemyCount: Int,
        val spawnIntervalMs: Long,
        val enemyVelocity: Offset,
        val waveDelayMs: Long
    )

    private val waves = listOf(
        Wave(5, 1500L, Offset(0f, 100f), 3000L),
        Wave(7, 1000L, Offset(0f, 150f), 3000L),
        Wave(10, 800L, Offset(0f, 200f), 3000L)
    )

    suspend fun start() {
        while (holder.state.value.isRunning) {
            for (wave in waves) {
                if (!holder.state.value.isRunning) break
                for (i in 0 until wave.enemyCount) {
                    if (!holder.state.value.isRunning) break
                    val newEnemy = Enemy(
                        pos = Offset(rnd.nextInt(0, 400).toFloat(), -50f),
                        velocity = wave.enemyVelocity.copy(x = rnd.nextFloat() * 200f - 100f) // Random horizontal velocity
                    )
                    holder.update { copy(enemies = enemies + newEnemy) }
                    delay(wave.spawnIntervalMs)
                }
                delay(wave.waveDelayMs)
            }
            // After all waves, perhaps loop or introduce a final boss wave
            // For now, let's just loop the existing waves for continuous play
            delay(5000L) // Delay before repeating all waves
        }
    }
}
