package com.flightgame.game

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.flightgame.model.GameStateHolder
import kotlinx.coroutines.delay
// isActive removed to avoid coroutine scope checks in this simplified runner

/**
 * Main game loop running at approx 60 FPS.
 */
class GameRunner(
    private val holder: GameStateHolder,
    private val viewModel: GameViewModel
) {
    private val tickMs = 16L
    private val screenWidth = 480f

    suspend fun start() {
        while (holder.state.value.isRunning) {
            val delta = tickMs / 1000f
            holder.update {
                var a = aircraft
                val newPos = a.pos + a.velocity * delta
                val aircraftRect = Rect(newPos, newPos + Offset(a.width, a.height))

                // Move projectiles
                var updatedProjectiles = projectiles.map { p ->
                    p.copy(pos = p.pos + p.velocity * delta)
                }

                // Move enemies
                val updatedEnemies = enemies.map { e ->
                    var newEnemyPos = e.pos + e.velocity * delta
                    var newEnemyVel = e.velocity
                    if (newEnemyPos.x < 0f || newEnemyPos.x > screenWidth - 50f) {
                        newEnemyVel = e.velocity.copy(x = -e.velocity.x)
                        newEnemyPos = e.pos + newEnemyVel * delta // Recalculate position with new velocity
                    }
                    e.copy(pos = newEnemyPos, velocity = newEnemyVel)
                }

                // Detect projectile-enemy collisions
                val (hitProjectiles, hitEnemies) = CollisionDetector.detect(updatedProjectiles, updatedEnemies)
                var finalEnemies = updatedEnemies
                if (hitEnemies.isNotEmpty()) {
                    viewModel.playExplosionSound()
                    updatedProjectiles = updatedProjectiles - hitProjectiles.toSet()
                    finalEnemies = updatedEnemies - hitEnemies.toSet()
                }

                // Move and collect power-ups
                var updatedPowerUps = powerUps.map { p ->
                    p.copy(pos = p.pos + p.velocity * delta)
                }
                val collectedPowerUps = updatedPowerUps.filter { powerUp ->
                    val powerUpRect = Rect(powerUp.pos, powerUp.pos + Offset(30f, 30f)) // Assuming 30x30 size
                    aircraftRect.overlaps(powerUpRect)
                }
                if (collectedPowerUps.isNotEmpty()) {
                    viewModel.playPowerUpSound()
                    updatedPowerUps = updatedPowerUps - collectedPowerUps.toSet()
                    a = a.copy(hasShield = true)
                }


                // Detect player-enemy collisions and handle shield
                var isGameOver = false
                val enemiesAfterPlayerCollision = finalEnemies.toMutableList()
                val playerHitEnemy = finalEnemies.find { enemy ->
                    val enemyRect = Rect(enemy.pos, enemy.pos + Offset(50f, 50f))
                    aircraftRect.overlaps(enemyRect)
                }

                if (playerHitEnemy != null) {
                    viewModel.playExplosionSound()
                    if (a.hasShield) {
                        a = a.copy(hasShield = false)
                        enemiesAfterPlayerCollision.remove(playerHitEnemy)
                    } else {
                        isGameOver = true
                    }
                }
                finalEnemies = enemiesAfterPlayerCollision

                if (!isGameOver) {
                    isGameOver = finalEnemies.any { it.pos.y > 800f }
                }

                copy(
                    aircraft = a.copy(pos = newPos),
                    projectiles = updatedProjectiles,
                    enemies = finalEnemies,
                    powerUps = updatedPowerUps,
                    score = score + hitEnemies.size,
                    isRunning = !isGameOver
                )
            }
            delay(tickMs)
        }
    }
}

private operator fun Offset.plus(o: Offset) = Offset(x + o.x, y + o.y)
private operator fun Offset.times(scalar: Float) = Offset(x * scalar, y * scalar)
private fun Offset.copy(x: Float = this.x, y: Float = this.y) = Offset(x, y)
