package com.flightgame.game

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightgame.model.GameState
import com.flightgame.model.GameStateHolder
import com.flightgame.model.Projectile
import kotlinx.coroutines.launch

/**
 * ViewModel that orchestrates the game loop and exposes a Flow of GameState.
 */
class GameViewModel : ViewModel() {
    private var stateHolder = GameStateHolder()
    private val repository = GameRepository(stateHolder)
    val stateFlow = repository.state

    private var soundManager: SoundManager? = null

    private var runner = GameRunner(stateHolder, this)
    private var enemySpawner = EnemySpawner(stateHolder)
    private var powerUpSpawner = PowerUpSpawner(stateHolder)
    private val inputHandler = InputHandler(stateHolder)

    init {
        startGameLoops()
    }

    fun initSoundManager(context: Context) {
        soundManager = SoundManager(context)
    }

    private fun startGameLoops() {
        viewModelScope.launch { runner.start() }
        viewModelScope.launch { enemySpawner.start() }
        viewModelScope.launch { powerUpSpawner.start() }
    }

    fun onDrag(dragAmount: Offset) {
        inputHandler.onDrag(dragAmount)
    }

    fun fire() {
        soundManager?.playShootSound()
        stateHolder.update {
            val newProjectile = Projectile(
                pos = aircraft.pos,
                velocity = Offset(0f, -1000f) // Move up
            )
            copy(projectiles = projectiles + newProjectile)
        }
    }

    fun playExplosionSound() {
        soundManager?.playExplosionSound()
    }

    fun playPowerUpSound() {
        soundManager?.playPowerUpSound()
    }

    fun restart() {
        stateHolder.update { GameState() } // Reset to default state
        runner = GameRunner(stateHolder, this)
        enemySpawner = EnemySpawner(stateHolder)
        powerUpSpawner = PowerUpSpawner(stateHolder)
        startGameLoops()
    }

    /**
     * Test-only function to force the game into a game-over state.
     */
    fun setGameOverForTest() {
        stateHolder.update { copy(isRunning = false) }
    }
}
