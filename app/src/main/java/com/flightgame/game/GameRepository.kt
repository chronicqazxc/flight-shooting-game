package com.flightgame.game

import com.flightgame.model.GameStateHolder

/**
 * Repository exposing the shared GameState.
 */
class GameRepository(private val holder: GameStateHolder) {
    val state = holder.state
    /**
     * Proxy to {@link GameStateHolder#update}.
     */
    fun update(block: com.flightgame.model.GameState.() -> com.flightgame.model.GameState){
        // The block should be a receiver lambda.  The repository just forwards
        // it to the underlying state holder.
        holder.update(block)
    }
}
