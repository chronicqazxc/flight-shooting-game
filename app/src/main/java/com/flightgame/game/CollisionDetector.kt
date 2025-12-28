package com.flightgame.game

import androidx.compose.ui.geometry.Rect
import com.flightgame.model.Enemy
import com.flightgame.model.Projectile

object CollisionDetector {
    fun detect(projectiles: List<Projectile>, enemies: List<Enemy>): Pair<List<Projectile>, List<Enemy>> {
        val hitProjectiles = mutableSetOf<Projectile>()
        val hitEnemies = mutableSetOf<Enemy>()

        projectiles.forEach { projectile ->
            val projectileBounds = Rect(
                left = projectile.pos.x - 10f,
                top = projectile.pos.y - 10f,
                right = projectile.pos.x + 10f,
                bottom = projectile.pos.y + 10f
            )

            enemies.forEach { enemy ->
                if (!hitEnemies.contains(enemy)) {
                    val enemyBounds = Rect(
                        left = enemy.pos.x,
                        top = enemy.pos.y,
                        right = enemy.pos.x + 50f,
                        bottom = enemy.pos.y + 50f
                    )

                    if (projectileBounds.overlaps(enemyBounds)) {
                        hitProjectiles.add(projectile)
                        hitEnemies.add(enemy)
                    }
                }
            }
        }
        return Pair(hitProjectiles.toList(), hitEnemies.toList())
    }
}
