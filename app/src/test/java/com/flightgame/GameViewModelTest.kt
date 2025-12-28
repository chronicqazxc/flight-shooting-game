package com.flightgame

import app.cash.turbine.test
import com.flightgame.game.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GameViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fire adds a projectile to the game state`() = runTest {
        // Given
        val viewModel = GameViewModel()
        val initialProjectileCount = viewModel.stateFlow.value.projectiles.size

        // When
        viewModel.fire()

        // Then
        val newProjectileCount = viewModel.stateFlow.value.projectiles.size
        assertEquals(initialProjectileCount + 1, newProjectileCount)
    }
}
