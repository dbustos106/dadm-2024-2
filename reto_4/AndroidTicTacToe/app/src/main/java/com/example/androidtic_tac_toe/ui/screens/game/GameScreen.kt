package com.example.androidtic_tac_toe.ui.screens.game

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidtic_tac_toe.R
import com.example.androidtic_tac_toe.shared.SharedViewModel
import com.example.androidtic_tac_toe.ui.screens.game.components.ComputerSection
import com.example.androidtic_tac_toe.ui.screens.game.components.DifficultyDialog
import com.example.androidtic_tac_toe.ui.screens.game.components.GameBoard
import com.example.androidtic_tac_toe.ui.screens.game.components.ScoreItem
import com.example.androidtic_tac_toe.ui.screens.game.components.TopBar
import com.example.androidtic_tac_toe.ui.screens.game.events.GameUiEvent
import com.example.androidtic_tac_toe.ui.screens.game.events.GameViewModelEvent

/**
 * Composable function representing the main game screen.
 * It includes the game board, options button, and displays the current game state.
 */
@Composable
fun GameScreen(
    gameMode: GameMode,
    onClickReturnHome: () -> Unit,
    sharedViewModel: SharedViewModel = viewModel(),
    gameViewModel: GameViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Variables to manage UI events
    var showDifficultyDialog by rememberSaveable { mutableStateOf(true) }
    var isSoundEnabled by rememberSaveable { mutableStateOf(true) }

    // Observer of the game state flow
    val gameUiState by gameViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                isSoundEnabled = isSoundEnabled,
                onClickToggleSound = { isSoundEnabled = !isSoundEnabled },
                onClickReturnHome = onClickReturnHome,
                onClickStartNewGame = { gameViewModel.onEvent(GameUiEvent.StartNewGame) },
                onClickChangeDifficulty = { showDifficultyDialog = true }
            )
        },
        content = { innerPadding ->
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(innerPadding)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {

                    val opacity = when {
                        gameUiState.isGameOver -> 0.4f
                        gameUiState.currentPlayer == Player.HUMAN -> 0.4f
                        else -> 1f
                    }

                    ComputerSection(
                        isGameOver = gameUiState.isGameOver,
                        currentPlayer = gameUiState.currentPlayer,
                        modifier = Modifier
                            .padding(start = 30.dp)
                            .alpha(opacity)
                    )

                    GameBoard(
                        isGameOver = gameUiState.isGameOver,
                        currentUser = gameUiState.currentPlayer,
                        board = gameUiState.board,
                        onClickSquare = { location ->
                            gameViewModel.onEvent(GameUiEvent.MakeHumanMove(location))
                        },
                        modifier = Modifier.padding(25.dp)
                    )

                    val infoText = when (gameUiState.state) {
                        GameState.TIE -> stringResource(R.string.text_state_tie)
                        GameState.WINNER_HUMAN -> stringResource(R.string.text_state_human_won)
                        GameState.WINNER_COMPUTER -> stringResource(R.string.text_state_android_won)
                        else -> {
                            if (gameUiState.currentPlayer == Player.COMPUTER) stringResource(R.string.text_state_android_turn)
                            else stringResource(R.string.text_state_human_turn)
                        }
                    }

                    Text(
                        text = infoText,
                        fontSize = 18.sp,
                        color = colorScheme.onBackground,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 30.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(colorScheme.surfaceVariant)
                        .padding(16.dp)
                ) {
                    ScoreItem(
                        score = gameUiState.numberHumanWins,
                        label = stringResource(R.string.text_human_score),
                        Modifier.weight(1f)
                    )
                    ScoreItem(
                        score = gameUiState.numberComputerWins,
                        label = stringResource(R.string.text_android_score),
                        Modifier.weight(1f)
                    )
                    ScoreItem(
                        score = gameUiState.numberTies,
                        label = stringResource(R.string.text_tie_score),
                        Modifier.weight(1f)
                    )
                }

            }

        },
        modifier = modifier
    )

    if (showDifficultyDialog) {
        DifficultyDialog(
            onDismiss = { showDifficultyDialog = false },
            onDifficultySelected = { difficultyLevel ->
                gameViewModel.onEvent(GameUiEvent.ChangeDifficultyLevel(difficultyLevel))
                showDifficultyDialog = false
            },
            currentDifficultyLevel = gameUiState.difficultyLevel,
        )
    }

    LaunchedEffect(key1 = LocalContext.current) {
        gameViewModel.playSoundEvent.collect { event ->
            try {
                if (isSoundEnabled) {
                    when (event) {
                        is GameViewModelEvent.PlayHumanSound -> {
                            sharedViewModel.playHumanSound()
                        }

                        is GameViewModelEvent.PlayComputerSound -> {
                            sharedViewModel.playComputerSound()
                        }
                    }
                }
            } catch (e: IllegalStateException) {
                Log.e("Sound", "Error ${e.message}")
            }
        }
    }

}
