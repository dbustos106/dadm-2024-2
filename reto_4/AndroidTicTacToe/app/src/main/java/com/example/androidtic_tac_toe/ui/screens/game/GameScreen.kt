package com.example.androidtic_tac_toe.ui.screens.game

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidtic_tac_toe.ui.screens.game.components.ComputerSection
import com.example.androidtic_tac_toe.ui.screens.game.components.DifficultyDialog
import com.example.androidtic_tac_toe.ui.screens.game.components.GameBoard
import com.example.androidtic_tac_toe.ui.screens.game.components.ScoreSection
import com.example.androidtic_tac_toe.ui.screens.game.components.TopBar
import com.example.androidtic_tac_toe.ui.screens.game.events.GameUiEvent

/**
 * Composable function representing the main game screen.
 * It includes the game board, options button, and displays the current game state.
 */
@Composable
fun GameScreen(
    gameMode: GameMode,
    gameViewModel: GameViewModel = hiltViewModel(),
    onClickReturnHome: () -> Unit,
    modifier: Modifier = Modifier
) {

    val gameUiState by gameViewModel.uiState.collectAsState()
    var showDifficultyDialog by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopBar(
                soundEnabled = gameUiState.soundEnabled,
                onEvent = { gameViewModel.onEvent(it) },
                onClickOpenDifficultyDialog = { showDifficultyDialog = true },
                onClickReturnHome = { onClickReturnHome() }
            )
        },
        content = { innerPadding ->
            when (LocalConfiguration.current.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    PortraitGameScreen(
                        gameMode = gameMode,
                        gameUiState = gameUiState,
                        onEvent = { gameViewModel.onEvent(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
                else -> {
                    LandscapeGameScreen(
                        gameMode = gameMode,
                        gameUiState = gameUiState,
                        onEvent = { gameViewModel.onEvent(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        },
        modifier = modifier
    )

    if (showDifficultyDialog && !gameUiState.loading) {
        DifficultyDialog(
            onDismiss = { showDifficultyDialog = false },
            onClickSetDifficultyLevel = { difficultyLevel ->
                gameViewModel.onEvent(GameUiEvent.SetDifficultyLevel(difficultyLevel))
            },
            currentDifficultyLevel = gameUiState.difficultyLevel
        )
    }
}

/**
 * Composable for rendering the game screen in portrait orientation.
 */
@Composable
fun PortraitGameScreen(
    gameMode: GameMode,
    gameUiState: GameUiState,
    onEvent: (GameUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {

            ComputerSection(
                gameState = gameUiState.gameState,
                currentPlayer = gameUiState.currentPlayer,
                modifier = Modifier
                    .padding(start = 30.dp)
            )

            GameBoard(
                isGameOver = gameUiState.gameOver,
                currentUser = gameUiState.currentPlayer,
                board = gameUiState.board,
                onClickSquare = { location ->
                    onEvent(GameUiEvent.MakeHumanMove(location))
                },
                modifier = Modifier.padding(25.dp)
            )

        }

        ScoreSection(
            numberTies = gameUiState.numberTies,
            numberHumanWins = gameUiState.numberHumanWins,
            numberComputerWins = gameUiState.numberComputerWins,
            modifier = Modifier
                .background(colorScheme.surfaceVariant)
                .padding(16.dp)
        )
    }

}

/**
 * Composable for rendering the game screen in landscape orientation.
 */
@Composable
fun LandscapeGameScreen(
    gameMode: GameMode,
    gameUiState: GameUiState,
    onEvent: (GameUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ){
        GameBoard(
            isGameOver = gameUiState.gameOver,
            currentUser = gameUiState.currentPlayer,
            board = gameUiState.board,
            onClickSquare = { location ->
                onEvent(GameUiEvent.MakeHumanMove(location))
            },
            modifier = Modifier.padding(start = 40.dp, bottom = 16.dp, end = 16.dp, top = 16.dp)
        )

        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ){
            ComputerSection(
                gameState = gameUiState.gameState,
                currentPlayer = gameUiState.currentPlayer,
                modifier = Modifier
                    .padding(start = 30.dp)
            )

            ScoreSection(
                numberTies = gameUiState.numberTies,
                numberHumanWins = gameUiState.numberHumanWins,
                numberComputerWins = gameUiState.numberComputerWins,
                modifier = Modifier
                    .background(colorScheme.surfaceVariant)
                    .padding(16.dp)
            )
        }
    }

}