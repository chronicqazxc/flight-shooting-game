package com.flightgame.model

import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Holds all mutable game state.
 */
data class GameState(
    val aircraft: Aircraft = Aircraft(pos = Offset(200f, 400f)),
    val enemies: List<Enemy> = emptyList(),
    val projectiles: List<Projectile> = emptyList(),
    val powerUps: List<PowerUp> = emptyList(),
    val score: Int = 0,
    val isRunning: Boolean = true
)

class GameStateHolder(initial: GameState = GameState()) {
    private val _state = MutableStateFlow(initial)
    val state: StateFlow<GameState> = _state
    fun update(block: GameState.() -> GameState) {
        _state.value = _state.value.block()
    }
}
