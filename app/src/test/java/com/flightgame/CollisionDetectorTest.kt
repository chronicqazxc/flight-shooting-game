package com.flightgame

import androidx.compose.ui.geometry.Offset
import com.flightgame.game.CollisionDetector
import com.flightgame.model.Enemy
import com.flightgame.model.Projectile
import org.junit.Assert.assertEquals
import org.junit.Test

class CollisionDetectorTest {

    @Test
    fun `detect returns hit objects when projectile overlaps enemy`() {
        // Given
        val projectile = Projectile(pos = Offset(100f, 100f))
        val enemy = Enemy(pos = Offset(80f, 80f)) // Overlaps the 20x20 projectile centered at 100,100

        // When
        val (hitProjectiles, hitEnemies) = CollisionDetector.detect(listOf(projectile), listOf(enemy))

        // Then
        assertEquals(1, hitProjectiles.size)
        assertEquals(1, hitEnemies.size)
        assertEquals(projectile, hitProjectiles.first())
        assertEquals(enemy, hitEnemies.first())
    }

    @Test
    fun `detect returns empty lists when no objects overlap`() {
        // Given
        val projectile = Projectile(pos = Offset(100f, 100f))
        val enemy = Enemy(pos = Offset(300f, 300f)) // Far away

        // When
        val (hitProjectiles, hitEnemies) = CollisionDetector.detect(listOf(projectile), listOf(enemy))

        // Then
        assertEquals(0, hitProjectiles.size)
        assertEquals(0, hitEnemies.size)
    }

    @Test
    fun `detect handles multiple objects correctly`() {
        // Given
        val projectile1 = Projectile(pos = Offset(100f, 100f)) // Hits enemy1
        val projectile2 = Projectile(pos = Offset(500f, 500f)) // Hits no one
        val enemy1 = Enemy(pos = Offset(80f, 80f))
        val enemy2 = Enemy(pos = Offset(900f, 900f)) // Is not hit

        // When
        val (hitProjectiles, hitEnemies) = CollisionDetector.detect(listOf(projectile1, projectile2), listOf(enemy1, enemy2))

        // Then
        assertEquals(1, hitProjectiles.size)
        assertEquals(1, hitEnemies.size)
        assertEquals(projectile1, hitProjectiles.first())
        assertEquals(enemy1, hitEnemies.first())
    }
}
