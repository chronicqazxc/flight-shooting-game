package com.flightgame.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.flightgame.game.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Basic game screen that displays the current score and handles input.
 */
@Composable
fun GameScreen(viewModel: GameViewModel = viewModel(), modifier: Modifier = Modifier) {
    val state = viewModel.stateFlow.collectAsState()

    if (state.value.isRunning) {
        Box(modifier = modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            viewModel.onDrag(dragAmount)
                        }
                    },
            ) {
                val aircraft = state.value.aircraft
                drawRect(
                    color = Color.Blue,
                    topLeft = aircraft.pos,
                    size = androidx.compose.ui.geometry.Size(aircraft.width, aircraft.height)
                )

                if (aircraft.hasShield) {
                    drawCircle(
                        color = Color.Blue.copy(alpha = 0.5f),
                        radius = 50f,
                        center = aircraft.pos + androidx.compose.ui.geometry.Offset(aircraft.width / 2, aircraft.height / 2)
                    )
                }

                state.value.projectiles.forEach { projectile ->
                    drawCircle(
                        color = Color.Red,
                        radius = 10f,
                        center = projectile.pos
                    )
                }

                state.value.enemies.forEach { enemy ->
                    drawRect(
                        color = Color.Green,
                        topLeft = enemy.pos,
                        size = androidx.compose.ui.geometry.Size(50f, 50f)
                    )
                }

                state.value.powerUps.forEach { powerUp ->
                    drawCircle(
                        color = Color.Yellow,
                        radius = 15f,
                        center = powerUp.pos
                    )
                }
            }

            Text(
                text = "Score: ${state.value.score}",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )

            Button(
                onClick = { viewModel.fire() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("Fire")
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Game Over")
                Text(text = "Final Score: ${state.value.score}")
                Button(onClick = { viewModel.restart() }) {
                    Text("Restart")
                }
            }
        }
    }
}
