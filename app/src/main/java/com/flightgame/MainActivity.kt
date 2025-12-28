package com.flightgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.flightgame.game.GameViewModel
import com.flightgame.ui.game.GameScreen

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.initSoundManager(this)
        setContent {
            GameScreen(viewModel = viewModel)
        }
    }
}
