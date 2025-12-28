package com.flightgame.game

import androidx.compose.ui.geometry.Offset
import com.flightgame.model.GameStateHolder

class InputHandler(private val stateHolder: GameStateHolder) {
    fun onDrag(dragAmount: Offset) {
        stateHolder.update {
            val newPos = aircraft.pos + dragAmount
            copy(aircraft = aircraft.copy(pos = newPos))
        }
    }
}
