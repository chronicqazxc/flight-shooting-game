package com.flightgame

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.flightgame.game.GameViewModel
import com.flightgame.ui.game.GameScreen
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class GameScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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
    fun gameScreen_initialState_elementsAreDisplayed() {
        composeTestRule.mainClock.autoAdvance = false // Disable auto-advancing

        val viewModel = GameViewModel(coroutineDispatcher = testDispatcher)
        viewModel.initSoundManager(InstrumentationRegistry.getInstrumentation().targetContext)

        composeTestRule.setContent {
            GameScreen(viewModel = viewModel)
        }

        // Initially, the game loops have started, but no time has passed.
        testDispatcher.scheduler.runCurrent() // Run any pending coroutines to initialize
        
        composeTestRule.onNodeWithText("Score: 0").assertIsDisplayed()
        composeTestRule.onNodeWithText("Fire").assertIsDisplayed()
    }

    @Test
    fun gameScreen_fireButton_producesClick() {
        composeTestRule.mainClock.autoAdvance = false // Disable auto-advancing

        val viewModel = GameViewModel(coroutineDispatcher = testDispatcher)
        viewModel.initSoundManager(InstrumentationRegistry.getInstrumentation().targetContext)

        composeTestRule.setContent {
            GameScreen(viewModel = viewModel)
        }
        
        testDispatcher.scheduler.runCurrent()

        composeTestRule.onNodeWithText("Fire").performClick()
        
        // After clicking, advance time slightly to process the click and any resulting state change
        composeTestRule.mainClock.advanceTimeBy(100L)
        
        composeTestRule.onNodeWithText("Fire").assertIsDisplayed() // Still displayed after click
    }

    @Test
    fun gameScreen_gameOverState_displaysGameOverAndRestart() {
        composeTestRule.mainClock.autoAdvance = false // Disable auto-advancing
        
        val viewModel = GameViewModel(coroutineDispatcher = testDispatcher)
        viewModel.initSoundManager(InstrumentationRegistry.getInstrumentation().targetContext)

        composeTestRule.setContent {
            GameScreen(viewModel = viewModel)
        }

        testDispatcher.scheduler.runCurrent()
        
        // Force game over state
        viewModel.setGameOverForTest()
        
        // Advance time to allow UI to recompose after state change
        composeTestRule.mainClock.advanceTimeBy(100L)

        // Verify "Game Over" and "Restart" elements are displayed
        composeTestRule.onNodeWithText("Game Over", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Final Score:", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Restart", ignoreCase = true).assertIsDisplayed()

        // Click restart and verify initial state elements reappear
        composeTestRule.onNodeWithText("Restart", ignoreCase = true).performClick()
        
        // Advance time to allow UI to recompose after restart
        composeTestRule.mainClock.advanceTimeBy(100L)
        
        composeTestRule.onNodeWithText("Score:", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Fire").assertIsDisplayed()
    }
}
