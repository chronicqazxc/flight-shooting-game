package com.flightgame

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
import org.junit.Assert.assertNotNull

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

        // Wait until the "Score:" text is displayed and verify existence and display
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Score:", substring = true).fetchSemanticsNodeOrNull() != null
        }
        composeTestRule.onNodeWithText("Score:", substring = true).assertIsDisplayed()

        // Wait until the "Fire" button is displayed and verify existence and display
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Fire").fetchSemanticsNodeOrNull() != null
        }
        composeTestRule.onNodeWithText("Fire").assertIsDisplayed()
    }

    @Test
    fun gameScreen_fireButton_producesClick() {
        composeTestRule.onRoot().printToLog("fireButtonTest")
        // Wait until the "Fire" button is displayed before clicking
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Fire").fetchSemanticsNodeOrNull() != null
        }
        composeTestRule.onNodeWithText("Fire").performClick()
        composeTestRule.onNodeWithText("Fire").assertIsDisplayed() // Still displayed after click
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
            composeTestRule.onNodeWithText("Game Over", ignoreCase = true).fetchSemanticsNodeOrNull() != null
        }
        composeTestRule.onNodeWithText("Game Over", ignoreCase = true).assertIsDisplayed()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Final Score:", substring = true).fetchSemanticsNodeOrNull() != null
        }
        composeTestRule.onNodeWithText("Final Score:", substring = true).assertIsDisplayed()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Restart", ignoreCase = true).fetchSemanticsNodeOrNull() != null
        }
        composeTestRule.onNodeWithText("Restart", ignoreCase = true).assertIsDisplayed()

        // Click restart and verify initial state elements reappear
        composeTestRule.onNodeWithText("Restart", ignoreCase = true).performClick()
        composeTestRule.waitForIdle()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Score:", substring = true).fetchSemanticsNodeOrNull() != null
        }
        composeTestRule.onNodeWithText("Score:", substring = true).assertIsDisplayed()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onNodeWithText("Fire").fetchSemanticsNodeOrNull() != null
        }
        composeTestRule.onNodeWithText("Fire").assertIsDisplayed()
    }
}