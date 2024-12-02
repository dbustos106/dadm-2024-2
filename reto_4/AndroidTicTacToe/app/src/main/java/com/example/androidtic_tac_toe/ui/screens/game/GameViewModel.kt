package com.example.androidtic_tac_toe.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtic_tac_toe.data.UserPreferencesRepository
import com.example.androidtic_tac_toe.data.model.DifficultyLevel
import com.example.androidtic_tac_toe.shared.MediaPlayerManager
import com.example.androidtic_tac_toe.ui.screens.game.events.GameUiEvent
import com.example.androidtic_tac_toe.ui.screens.game.events.GameViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

const val BOARD_SIZE = 9

/**
 * ViewModel for managing Tic-Tac-Toe game logic and UI state updates.
 */
@HiltViewModel
class GameViewModel @Inject constructor(
    private val mediaPlayerManager: MediaPlayerManager,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // ViewModel events
    private val _viewModelEvent = MutableSharedFlow<GameViewModelEvent>(replay = 0)
    val viewModelEvent: SharedFlow<GameViewModelEvent> = _viewModelEvent.asSharedFlow()

    // UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    /**
     * Initializes a new game and load the user preferences.
     */
    init {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collect { userPreferences ->
                _uiState.update { currentState ->
                    currentState.copy(
                        difficultyLevel = userPreferences.difficultyLevel,
                        soundEnabled = userPreferences.soundEnabled,
                        numberComputerWins = userPreferences.numberComputerWins,
                        numberHumanWins = userPreferences.numberHumanWins,
                        numberTies = userPreferences.numberTies,
                        loading = false
                    )
                }
            }
        }

        startNewGame()
    }

    /**
     * Handle various UI events triggered bt the user.
     * @param event The UI event to handle.
     */
    fun onEvent(event: GameUiEvent) {
        when(event) {
            is GameUiEvent.StartNewGame -> {
                startNewGame()
            }
            is GameUiEvent.ResetScores -> {
                resetScores()
            }
            is GameUiEvent.SetDifficultyLevel -> {
                setDifficultyLevel(event.difficultyLevel)
            }
            is GameUiEvent.SetSoundEnabled -> {
                setSoundEnabled(event.soundEnabled)
            }
            is GameUiEvent.MakeHumanMove -> {
                makeHumanMove(event.location)
            }
        }
    }

    /**
     * Starts a new game, setting a random starting player.
     * If the computer goes first, it makes an initial move.
     */
    private fun startNewGame() {
        // Reset the game state
        _uiState.update { currentState ->
            currentState.copy(
                gameState = GameState.NO_WINNER,
                currentPlayer = selectRandomPlayer(),
                board = List(9) { SquareState() },
                gameOver = false,
            )
        }

        // Make computer move if it’s its turn
        if(_uiState.value.currentPlayer == Player.COMPUTER) {
            viewModelScope.launch {
                delay(1000L)
                makeComputerMove()
            }
        }
    }

    /**
     * Selects a random player to start the game.
     */
    private fun selectRandomPlayer(): Player {
        if(Random.nextInt(2) == 0) {
            return Player.HUMAN
        }
        return Player.COMPUTER
    }

    /**
     * Reset the game scores
     */
    private fun resetScores() {
        viewModelScope.launch {
            userPreferencesRepository.updateNumberComputerWins(0)
            userPreferencesRepository.updateNumberHumanWins(0)
            userPreferencesRepository.updateNumberTies(0)
        }
    }

    /**
     * Sets the game's difficulty level.
     * @param difficultyLevel The difficulty level to set.
     */
    private fun setDifficultyLevel(difficultyLevel: DifficultyLevel) {
        viewModelScope.launch {
            userPreferencesRepository.updateDifficultyLevel(difficultyLevel)
        }
    }

    /**
     * Sets whether the sound is enabled or disabled.
     * @param soundEnabled A boolean indicating whether the sound is enabled.
     */
    private fun setSoundEnabled(soundEnabled: Boolean){
        viewModelScope.launch {
            userPreferencesRepository.updateSoundEnabled(soundEnabled)
        }
    }

    /**
     * Handles the human player's move, checks for a winner, and
     * initiates the computer's turn if the game is ongoing.
     * @param location The index on the game board (0 to 8)
     */
    private fun makeHumanMove(location: Int) {
        setMove(Player.HUMAN, location)

        // Make the computer move if the game is not over
        if(!_uiState.value.gameOver) {
            viewModelScope.launch {
                delay(1000L)
                makeComputerMove()
            }
        }
    }

    /**
     *  Makes the computer's move based on the current game state and difficulty level.
     *  It tries to win or block the player, otherwise selects a random open spot.
     */
    private fun makeComputerMove() {
        var blockingMove: Int? = null
        val board = _uiState.value.board.toMutableList()

        if(_uiState.value.difficultyLevel != DifficultyLevel.EASY) {
            for (i in 0 until BOARD_SIZE) {
                if (board[i].player == Player.OPEN_SPOT) {

                    // See if there's a move O can make to win
                    board[i] = board[i].copy(player = Player.COMPUTER)
                    if (checkGameState(board) == GameState.WINNER_COMPUTER) {
                        setMove(Player.COMPUTER, i)
                        return
                    }
                    board[i] = board[i].copy(player = Player.OPEN_SPOT)

                    // If the difficulty level is expert, add logic to block the player
                    if(_uiState.value.difficultyLevel == DifficultyLevel.EXPERT) {
                        board[i] = board[i].copy(player = Player.HUMAN)
                        if (checkGameState(board) == GameState.WINNER_HUMAN) {
                            blockingMove = i
                        }
                        board[i] = board[i].copy(player = Player.OPEN_SPOT)
                    }

                }
            }
        }

        val location = blockingMove ?: (0 until BOARD_SIZE).filter { board[it].player == Player.OPEN_SPOT }.random()
        setMove(Player.COMPUTER, location)
    }

    /**
     * Evaluates the current state of the game based on the board.
     * @param board A list of SquareState representing the current state of the game board.
     * @return The current game state: WINNER_HUMAN, WINNER_COMPUTER, TIE, or NO_WINNER.
     */
    private fun checkGameState(board: List<SquareState>): GameState {
        val winningLines = listOf (
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )

        for(line in winningLines) {
            if(board[line[0]].player == Player.HUMAN &&
                board[line[1]].player == Player.HUMAN &&
                board[line[2]].player == Player.HUMAN
            ) {
                return GameState.WINNER_HUMAN
            }
            if(board[line[0]].player == Player.COMPUTER &&
                board[line[1]].player == Player.COMPUTER &&
                board[line[2]].player == Player.COMPUTER
            ) {
                return GameState.WINNER_COMPUTER
            }
        }

        // Check for tie
        if(board.all { it.player == Player.HUMAN || it.player == Player.COMPUTER }) {
            return GameState.TIE
        }

        // No winner or tie yet
        return GameState.NO_WINNER
    }

    /**
     * Updates the board with the player's move at the specified location.
     * @param player The player making the move,
     * @param location The index on the game board (0 to 8).
     */
    private fun setMove(player: Player, location: Int) {
        // Make the move on a mutable copy of the board.
        val currentBoard = _uiState.value.board.toMutableList()
        currentBoard[location] = currentBoard[location].copy (
            player = player,
            enabled = false,
        )

        // Update the game state based on the move
        val gameState = checkGameState(currentBoard)
        _uiState.update { currentState ->
            currentState.copy (
                gameOver = (gameState == GameState.TIE || gameState == GameState.WINNER_HUMAN || gameState == GameState.WINNER_COMPUTER),
                currentPlayer = if (_uiState.value.currentPlayer == Player.HUMAN) Player.COMPUTER else Player.HUMAN,
                board = currentBoard,
                gameState = gameState,
            )
        }

        // Update the user preferences based on the move
        viewModelScope.launch {
            if (gameState == GameState.WINNER_HUMAN) userPreferencesRepository.updateNumberHumanWins(_uiState.value.numberHumanWins + 1)
            if (gameState == GameState.WINNER_COMPUTER) userPreferencesRepository.updateNumberComputerWins(_uiState.value.numberComputerWins + 1)
            if (gameState == GameState.TIE) userPreferencesRepository.updateNumberTies(_uiState.value.numberTies + 1)
        }

        // Emit event to start the music based on player
        viewModelScope.launch {
            if (_uiState.value.soundEnabled) {
                when (player) {
                    Player.HUMAN -> mediaPlayerManager.playHumanSound()
                    else -> mediaPlayerManager.playComputerSound()
                }
            }
        }
    }

}
