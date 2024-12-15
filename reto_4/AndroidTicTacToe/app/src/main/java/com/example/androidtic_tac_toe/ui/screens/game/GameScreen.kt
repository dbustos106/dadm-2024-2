package com.example.androidtic_tac_toe.ui.screens.game

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidtic_tac_toe.R
import com.example.androidtic_tac_toe.ui.screens.game.components.DifficultyDialog
import com.example.androidtic_tac_toe.ui.screens.game.components.GameBoard
import com.example.androidtic_tac_toe.ui.screens.game.components.GameLobbyDialog
import com.example.androidtic_tac_toe.ui.screens.game.components.InfoSection
import com.example.androidtic_tac_toe.ui.screens.game.components.MenuOption
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
    onReturnHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    var showDifficultyDialog by rememberSaveable { mutableStateOf(true) }
    var showGameLobbyDialog by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect (gameMode) {
        gameViewModel.onEvent(GameUiEvent.SetGameMode(gameMode))
    }

    BackHandler {
        gameViewModel.onEvent(GameUiEvent.DeleteOnlineGameIfNeeded)
        onReturnHomeClick()
    }

    val options = listOf(
        MenuOption(
            id = 0,
            name = stringResource(R.string.option_text_start_game),
            image = painterResource(id = R.drawable.startgame),
            onClick = { when(gameMode) {
                GameMode.SINGLE_PLAYER -> { showDifficultyDialog = true }
                GameMode.MULTIPLAYER -> {
                    gameViewModel.onEvent(GameUiEvent.DeleteOnlineGameIfNeeded)
                    showGameLobbyDialog = true
                }
            }}
        ),
        MenuOption(
            id = 1,
            name = stringResource(R.string.option_text_reset_scores),
            image = painterResource(id = R.drawable.reset_scores),
            onClick = { gameViewModel.onEvent(GameUiEvent.ResetScores) }
        )
    )

    if (gameUiState.userPreferences != null) {
        Scaffold(
            topBar = {
                TopBar(
                    options = options,
                    soundEnabled = gameUiState.userPreferences!!.soundEnabled,
                    onToggleSound = { gameViewModel.onEvent(GameUiEvent.SetSoundEnabled(it)) },
                    onReturnHomeClick = {
                        gameViewModel.onEvent(GameUiEvent.DeleteOnlineGameIfNeeded)
                        onReturnHomeClick()
                    },
                )
            },
            content = { innerPadding ->
                if (gameUiState.selectedGame != null){
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
                }

                when {
                    showDifficultyDialog && gameMode == GameMode.SINGLE_PLAYER -> {
                        DifficultyDialog(
                            onDismiss = {
                                showDifficultyDialog = false
                                gameViewModel.onEvent(GameUiEvent.StartNewLocalGame)
                            },
                            onClickSetDifficultyLevel = { difficultyLevel ->
                                gameViewModel.onEvent(GameUiEvent.SetDifficultyLevel(difficultyLevel))
                            },
                            currentDifficultyLevel = gameUiState.userPreferences!!.difficultyLevel
                        )
                    }
                    showGameLobbyDialog && gameMode == GameMode.MULTIPLAYER -> {
                        gameViewModel.onEvent(GameUiEvent.StartObservingAvailableGames)
                        GameLobbyDialog(
                            availableGames = gameUiState.availableGames,
                            onGameSelected = { gameId ->
                                gameViewModel.onEvent(GameUiEvent.StopObservingAvailableGames)
                                gameViewModel.onEvent(GameUiEvent.SelectOnlineGame(gameId))
                                showGameLobbyDialog = false
                            },
                            onCreateNewGameClick = {
                                gameViewModel.onEvent(GameUiEvent.StopObservingAvailableGames)
                                gameViewModel.onEvent(GameUiEvent.StartNewOnlineGame)
                                showGameLobbyDialog = false
                            },
                        )
                    }
                }
            },
            modifier = modifier
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
            InfoSection(
                userPlayer = gameUiState.userPlayer!!,
                gameMode = gameMode,
                gameState = gameUiState.selectedGame!!.gameState,
                currentPlayer = gameUiState.selectedGame.currentPlayer,
                modifier = Modifier.padding(start = 30.dp)
            )

            GameBoard(
                userPlayer = gameUiState.userPlayer,
                isGameOver = gameUiState.selectedGame.gameOver,
                currentUser = gameUiState.selectedGame.currentPlayer,
                board = gameUiState.selectedGame.board,
                onClickSquare = { location ->
                    onEvent(GameUiEvent.MakePlayerMove(location))
                },
                modifier = Modifier.padding(25.dp)
            )

            if(gameUiState.selectedGame.gameId != null) {
                Text(
                    text = stringResource(R.string.text_message_game_created, gameUiState.selectedGame.gameId.toString()),
                    style = typography.bodyMedium,
                    color = colorScheme.primary,
                    modifier = Modifier.padding(start = 30.dp)
                )
            }
        }

        ScoreSection(
            numberTies = gameUiState.userPreferences!!.numberTies,
            numberWins = gameUiState.userPreferences.numberWins,
            numberLosses = gameUiState.userPreferences.numberLosses,
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
            userPlayer = gameUiState.userPlayer!!,
            isGameOver = gameUiState.selectedGame!!.gameOver,
            currentUser = gameUiState.selectedGame.currentPlayer,
            board = gameUiState.selectedGame.board,
            onClickSquare = { location ->
                onEvent(GameUiEvent.MakePlayerMove(location))
            },
            modifier = Modifier.padding(start = 40.dp, bottom = 16.dp, end = 16.dp, top = 16.dp)
        )

        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            InfoSection(
                userPlayer = gameUiState.userPlayer,
                gameMode = gameMode,
                gameState = gameUiState.selectedGame.gameState,
                currentPlayer = gameUiState.selectedGame.currentPlayer,
                modifier = Modifier
                    .padding(start = 30.dp)
            )

            if(gameUiState.selectedGame.gameId != null) {
                Text(
                    text = stringResource(R.string.text_message_game_created, gameUiState.selectedGame.gameId.toString()),
                    style = typography.bodyMedium,
                    color = colorScheme.primary,
                    modifier = Modifier.padding(start = 30.dp)
                )
            }

            ScoreSection(
                numberTies = gameUiState.userPreferences!!.numberTies,
                numberWins = gameUiState.userPreferences.numberWins,
                numberLosses = gameUiState.userPreferences.numberLosses,
                modifier = Modifier
                    .clip(shapes.large)
                    .background(colorScheme.surfaceVariant)
                    .padding(16.dp)
            )
        }
    }

}
