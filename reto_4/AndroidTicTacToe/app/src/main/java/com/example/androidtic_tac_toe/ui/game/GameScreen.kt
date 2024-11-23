package com.example.androidtic_tac_toe.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidtic_tac_toe.R
import com.example.androidtic_tac_toe.ui.game.components.DifficultyDialog
import com.example.androidtic_tac_toe.ui.game.components.GameBar
import com.example.androidtic_tac_toe.ui.game.components.GameBoard
import com.example.androidtic_tac_toe.ui.game.components.GameComputerSection
import com.example.androidtic_tac_toe.ui.theme.AndroidTicTacToeTheme

/**
 * Composable function representing the main game screen.
 * It includes the game board, options button, and displays the current game state.
 */
@Composable
fun GameScreen(
    gameMode: GameMode,
    onClickReturnHome: () -> Unit = {},
    gameViewModel: GameViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var showDifficultyDialog by rememberSaveable { mutableStateOf(true) }
    val gameUiState by gameViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            GameBar(
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
                    val opacity = if (gameUiState.currentPlayer == Player.HUMAN) 0.4f else 1f
                    GameComputerSection(
                        currentPlayer = gameUiState.currentPlayer,
                        isGameOver = gameUiState.isGameOver,
                        modifier = Modifier
                            .padding(start = 30.dp)
                            .alpha(opacity)
                    )

                    GameBoard(
                        board = gameUiState.board,
                        isGameOver = gameUiState.isGameOver,
                        currentUser = gameUiState.currentPlayer,
                        onClickButton = { location ->
                            gameViewModel.onEvent(GameUiEvent.MakeHumanMove(location))
                        },
                        modifier = Modifier.padding(16.dp)
                    )

                    val infoText = when (gameUiState.state) {
                        GameState.TIE -> stringResource(R.string.es_un_empate)
                        GameState.WINNER_HUMAN -> stringResource(R.string.has_ganado)
                        GameState.WINNER_COMPUTER -> stringResource(R.string.android_ha_ganado)
                        else -> {
                            if (gameUiState.currentPlayer == Player.COMPUTER) stringResource(R.string.turno_de_android)
                            else stringResource(R.string.es_tu_turno_haz_una_buena_jugada)
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
                    modifier = Modifier.background(colorScheme.surfaceVariant)
                ) {
                    GameScoreItem(
                        score = gameUiState.numberHumanWins,
                        label = stringResource(R.string.tu),
                        Modifier.weight(1f)
                    )
                    GameScoreItem(
                        score = gameUiState.numberComputerWins,
                        label = stringResource(R.string.android),
                        Modifier.weight(1f)
                    )
                    GameScoreItem(
                        score = gameUiState.numberTies,
                        label = stringResource(R.string.empates),
                        Modifier.weight(1f)
                    )
                }

            }

        },
        modifier = modifier
    )

    if (showDifficultyDialog) {
        DifficultyDialog(
            onDifficultySelected = { difficultyLevel ->
                gameViewModel.onEvent(GameUiEvent.ChangeDifficultyLevel(difficultyLevel))
                showDifficultyDialog = false
            },
            onDismiss = { showDifficultyDialog = false },
            currentDifficultyLevel = gameUiState.difficultyLevel
        )
    }

}

/**
 * Shows an individual score item with a label and value.
 * Used in the game score summary.
 */
@Composable
fun GameScoreItem(score: Int, label: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = score.toString(), color = colorScheme.onSurfaceVariant)
        Text(text = label, color = colorScheme.onSurfaceVariant)
    }
}

/**
 * Preview function for the GameScreen composable.
 * Used for development previews in the IDE.
 */
@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    AndroidTicTacToeTheme {
        GameScreen(gameMode = GameMode.SINGLE_PLAYER)
    }
}
