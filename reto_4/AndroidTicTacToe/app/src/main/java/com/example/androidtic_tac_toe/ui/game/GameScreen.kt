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
import com.example.androidtic_tac_toe.ui.game.components.TopBar
import com.example.androidtic_tac_toe.ui.game.components.GameBoard
import com.example.androidtic_tac_toe.ui.game.components.ComputerSection
import com.example.androidtic_tac_toe.ui.game.components.ScoreItem
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
            TopBar(
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
                        onClickButton = { location ->
                            gameViewModel.onEvent(GameUiEvent.MakeHumanMove(location))
                        },
                        modifier = Modifier.padding(16.dp)
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
                    modifier = Modifier.background(colorScheme.surfaceVariant)
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
