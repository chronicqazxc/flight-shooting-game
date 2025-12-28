package com.flightgame

import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flightgame.MainActivity
import com.flightgame.game.GameViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import androidx.lifecycle.ViewModelProvider

@RunWith(AndroidJUnit4::class)
class GameScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var viewModel: GameViewModel

    @Before
    fun setup() {
        viewModel = ViewModelProvider(composeTestRule.activity).get(GameViewModel::class.java)
    }

    @Test
    fun gameScreen_initialState_elementsAreDisplayed() {
        composeTestRule.onRoot().printToLog("initialStateTest")
        // Wait until the ViewModel's state has emitted its initial value
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            viewModel.stateFlow.value.score == 0
        }

        // Wait until the "Score:" text is displayed
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Score:", substring = true).isDisplayed()
        }
        composeTestRule.onNodeWithText("Score:", substring = true).assertExists().assertIsDisplayed()

        // Wait until the "Fire" button is displayed
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Fire").isDisplayed()
        }
        composeTestRule.onNodeWithText("Fire").assertExists().assertIsDisplayed()
    }

    @Test
    fun gameScreen_fireButton_producesClick() {
        composeTestRule.onRoot().printToLog("fireButtonTest")
        // Wait until the "Fire" button is displayed before clicking
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Fire").isDisplayed()
        }
        composeTestRule.onNodeWithText("Fire").performClick()
        composeTestRule.onNodeWithText("Fire").assertExists().assertIsDisplayed() // Still displayed after click
    }

    @Test
    fun gameScreen_gameOverState_displaysGameOverAndRestart() {
        composeTestRule.onRoot().printToLog("gameOverStateTest")
        // Force game over state
        composeTestRule.activity.runOnUiThread {
            viewModel.setGameOverForTest()
        }
        composeTestRule.waitForIdle() // Wait for UI to recompose after state change

        // Verify "Game Over" and "Restart" elements are displayed
        composeTestRule.waitUntil(timeoutMillis = 60000) { // Increased timeout for game over state
            composeTestRule.onNodeWithText("Game Over", ignoreCase = true).isDisplayed()
        }
        composeTestRule.onNodeWithText("Game Over", ignoreCase = true).assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Final Score:", substring = true).assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Restart", ignoreCase = true).assertExists().assertIsDisplayed()

        // Click restart and verify initial state elements reappear
        composeTestRule.onNodeWithText("Restart", ignoreCase = true).performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Score:", substring = true).assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Fire").assertExists().assertIsDisplayed()
    }
}
