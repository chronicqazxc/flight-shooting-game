package com.flightgame.game

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightgame.model.GameState
import com.flightgame.model.GameStateHolder
import com.flightgame.model.Projectile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel that orchestrates the game loop and exposes a Flow of GameState.
 */
class GameViewModel(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    private var stateHolder = GameStateHolder()
    private val repository = GameRepository(stateHolder)
    val stateFlow = repository.state

    private var soundManager: SoundManager? = null

    private var runner: GameRunner? = null
    private var enemySpawner: EnemySpawner? = null
    private var powerUpSpawner: PowerUpSpawner? = null
    private val inputHandler = InputHandler(stateHolder)

    init {
        // Game loops will be explicitly started by tests or MainActivity
    }

    fun initSoundManager(context: Context) {
        soundManager = SoundManager(context)
    }

    fun startGameLoops() {
        runner = GameRunner(stateHolder, this)
        enemySpawner = EnemySpawner(stateHolder)
        powerUpSpawner = PowerUpSpawner(stateHolder)

        viewModelScope.launch(coroutineDispatcher) { runner?.start() }
        viewModelScope.launch(coroutineDispatcher) { enemySpawner?.start() }
        viewModelScope.launch(coroutineDispatcher) { powerUpSpawner?.start() }
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
        // Re-initialize and start game loops
        startGameLoops()
    }

    /**
     * Test-only function to force the game into a game-over state.
     */
    fun setGameOverForTest() {
        stateHolder.update { copy(isRunning = false) }
    }
}
