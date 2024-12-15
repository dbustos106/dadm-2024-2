package com.example.androidtic_tac_toe.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtic_tac_toe.data.GameRepository
import com.example.androidtic_tac_toe.data.UserPreferencesRepository
import com.example.androidtic_tac_toe.model.DifficultyLevel
import com.example.androidtic_tac_toe.model.Game
import com.example.androidtic_tac_toe.model.GameState
import com.example.androidtic_tac_toe.model.Player
import com.example.androidtic_tac_toe.model.SquareState
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
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

const val BOARD_SIZE = 9

/**
 * ViewModel for managing Tic-Tac-Toe game logic and UI state updates.
 */
@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
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
     * Load the user preferences.
     */
    init {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collect { userPreferences ->
                _uiState.update { currentState ->
                    currentState.copy(
                        userPreferences = userPreferences,
                        loading = false
                    )
                }
            }
        }
    }

    /**
     * Handle various UI events triggered bt the user.
     * @param event The UI event to handle.
     */
    fun onEvent(event: GameUiEvent) {
        when(event) {
            is GameUiEvent.SetGameMode -> {
                setGameMode(event.gameMode)
            }
            is GameUiEvent.StartNewLocalGame -> {
                startNewLocalGame()
            }
            is GameUiEvent.MakePlayerMove -> {
                makePlayerMove(event.location)
            }
            is GameUiEvent.SetDifficultyLevel -> {
                setDifficultyLevel(event.difficultyLevel)
            }
            is GameUiEvent.SetSoundEnabled -> {
                setSoundEnabled(event.soundEnabled)
            }
            is GameUiEvent.ResetScores -> {
                resetScores()
            }

            // Multiplayer functions
            is GameUiEvent.StartNewOnlineGame -> {
                startNewOnlineGame()
            }
            is GameUiEvent.StartObservingAvailableGames -> {
                startObservingAvailableGames()
            }
            is GameUiEvent.StopObservingAvailableGames -> {
                stopObservingAvailableGames()
            }
            is GameUiEvent.SelectOnlineGame -> {
                selectOnlineGame(event.gameId)
            }
            is GameUiEvent.DeleteOnlineGameIfNeeded -> {
                removeOnlineGameListener()
            }
        }
    }

    /**
     * Sets the game mode (e.g., single player or multiplayer).
     * @param gameMode The game mode to set.
     */
    private fun setGameMode(gameMode: GameMode){
        _uiState.update { currentState ->
            currentState.copy(
                gameMode = gameMode,
            )
        }
    }

    /**
     * Starts a new local game with the selected settings.
     */
    private fun startNewLocalGame() {
        _uiState.update { currentState ->
            currentState.copy(
                userPlayer = Player.X,
                selectedGame = Game(
                    gameId = null,
                    board = List(9) { SquareState() },
                    gameOver = false,
                    currentPlayer = selectRandomPlayer(),
                    gameState = GameState.NO_WINNER
                ),
            )
        }

        // Make computer move if it’s its turn
        if(_uiState.value.selectedGame!!.currentPlayer == Player.O) {
            viewModelScope.launch {
                delay(1000L)
                makeComputerMove()
            }
        }
    }

    /**
     * Selects a random starting player (either X or O).
     * @return The randomly selected player (X or O).
     */
    private fun selectRandomPlayer(): Player {
        if(Random.nextInt(2) == 0) {
            return Player.X
        }
        return Player.O
    }

    /**
     * Makes the player's move at the specified location.
     * @param location The index of the board where the player wants to place their move.
     */
    private fun makePlayerMove(location: Int) {
        setMove(_uiState.value.userPlayer!!, location)

        when(_uiState.value.gameMode) {
            GameMode.SINGLE_PLAYER -> {
                // Make the computer move if the game is not over,
                // and if the game mode is single player
                if (!_uiState.value.selectedGame!!.gameOver) {
                    viewModelScope.launch {
                        delay(1000L)
                        makeComputerMove()
                    }
                }
            }
            else -> {
                gameRepository.updateMove(
                    updatedGame = _uiState.value.selectedGame!!,
                    onFailure = { exception ->
                        _uiState.update { state ->
                            state.copy(errorMessage = exception.message)
                        }
                    }
                )
            }
        }
    }

    /**
     * Makes the computer's move if it's its turn.
     */
    private fun makeComputerMove() {
        var blockingMove: Int? = null
        val board = _uiState.value.selectedGame!!.board.toMutableList()

        if(_uiState.value.userPreferences!!.difficultyLevel != DifficultyLevel.EASY) {
            for (i in 0 until BOARD_SIZE) {
                if (board[i].player == Player.OPEN_SPOT) {

                    // See if there's a move O can make to win
                    board[i] = board[i].copy(player = Player.O)
                    if (checkGameState(board) == GameState.WINNER_O) {
                        setMove(Player.O, i)
                        return
                    }
                    board[i] = board[i].copy(player = Player.OPEN_SPOT)

                    // If the difficulty level is expert, add logic to block the player
                    if(_uiState.value.userPreferences!!.difficultyLevel == DifficultyLevel.EXPERT) {
                        board[i] = board[i].copy(player = Player.X)
                        if (checkGameState(board) == GameState.WINNER_X) {
                            blockingMove = i
                        }
                        board[i] = board[i].copy(player = Player.OPEN_SPOT)
                    }

                }
            }
        }

        val location = blockingMove ?: (0 until BOARD_SIZE).filter { board[it].player == Player.OPEN_SPOT }.random()
        setMove(Player.O, location)
    }

    /**
     * Checks the current game state based on the current board layout.
     * @param board The current state of the game board.
     * @return The current game state (e.g., winner, tie, or no winner).
     */
    private fun checkGameState(board: List<SquareState>): GameState {
        val winningLines = listOf (
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )

        for(line in winningLines) {
            if(board[line[0]].player == Player.X &&
                board[line[1]].player == Player.X &&
                board[line[2]].player == Player.X
            ) {
                return GameState.WINNER_X
            }
            if(board[line[0]].player == Player.O &&
                board[line[1]].player == Player.O &&
                board[line[2]].player == Player.O
            ) {
                return GameState.WINNER_O
            }
        }

        // Check for tie
        if(board.all { it.player == Player.X || it.player == Player.O }) {
            return GameState.TIE
        }

        // No winner or tie yet
        return GameState.NO_WINNER
    }

    /**
     * Sets the move for a player at a specific board location and updates the game state.
     * @param player The player making the move (X or O).
     * @param location The index of the board where the player makes the move.
     */
    private fun setMove(player: Player, location: Int) {
        val currentBoard = _uiState.value.selectedGame!!.board.toMutableList()
        currentBoard[location] = currentBoard[location].copy (
            player = player,
            enabled = false,
        )

        // Update the game state based on the move
        val gameState = checkGameState(currentBoard)
        _uiState.update { currentState ->
            currentState.copy (
                selectedGame = Game(
                    gameId = currentState.selectedGame?.gameId,
                    board = currentBoard,
                    gameOver = (gameState == GameState.TIE || gameState == GameState.WINNER_X || gameState == GameState.WINNER_O),
                    currentPlayer = if (_uiState.value.selectedGame!!.currentPlayer == Player.X) Player.O else Player.X,
                    gameState = gameState,
                ),
            )
        }

        updateScores(gameState)
        emitMediaPlayer(player)
    }

    /**
     * Updates the scores based on the game state (e.g., increment wins, losses, or ties).
     * @param gameState The current game state (e.g., winner, tie).
     */
    private fun updateScores(gameState: GameState){
        viewModelScope.launch {
            when (gameState) {
                GameState.WINNER_X -> {
                    if (_uiState.value.userPlayer == Player.X) userPreferencesRepository.updateNumberWins(_uiState.value.userPreferences!!.numberWins + 1)
                    else if (_uiState.value.userPlayer == Player.O) userPreferencesRepository.updateNumberLosses(_uiState.value.userPreferences!!.numberLosses + 1)
                }
                GameState.WINNER_O -> {
                    if (_uiState.value.userPlayer == Player.O) userPreferencesRepository.updateNumberWins(_uiState.value.userPreferences!!.numberWins + 1)
                    else if (_uiState.value.userPlayer == Player.X) userPreferencesRepository.updateNumberLosses(_uiState.value.userPreferences!!.numberLosses + 1)
                }
                GameState.TIE -> { userPreferencesRepository.updateNumberTies(_uiState.value.userPreferences!!.numberTies + 1) }
                else -> {}
            }
        }
    }

    /**
     * Emits a sound for the player who just made a move.
     * @param player The player (X or O) who made the move.
     */
    private fun emitMediaPlayer(player: Player){
        viewModelScope.launch {
            if (_uiState.value.userPreferences!!.soundEnabled) {
                when (player) {
                    Player.X -> mediaPlayerManager.playXSound()
                    else -> mediaPlayerManager.playOSound()
                }
            }
        }
    }

    /**
     * Sets the difficulty level for the game.
     * @param difficultyLevel The difficulty level to set.
     */
    private fun setDifficultyLevel(difficultyLevel: DifficultyLevel) {
        viewModelScope.launch {
            userPreferencesRepository.updateDifficultyLevel(difficultyLevel)
        }
    }

    /**
     * Enables or disables sound effects in the game.
     * @param soundEnabled A boolean indicating whether sound effects should be enabled or disabled.
     */
    private fun setSoundEnabled(soundEnabled: Boolean){
        viewModelScope.launch {
            userPreferencesRepository.updateSoundEnabled(soundEnabled)
        }
    }

    /**
     * Resets the scores (wins, losses, and ties) to zero.
     */
    private fun resetScores() {
        viewModelScope.launch {
            userPreferencesRepository.updateNumberLosses(0)
            userPreferencesRepository.updateNumberWins(0)
            userPreferencesRepository.updateNumberTies(0)
        }
    }



    // Multiplayer functions
    /**
     * Starts a new online game by creating a new game in the repository.
     */
    private fun startNewOnlineGame(){
        val newGame = Game(
            gameId = UUID.randomUUID().toString(),
            isAvailable = true,
            board = List(9) { SquareState() },
            currentPlayer = selectRandomPlayer(),
            gameState = GameState.NO_WINNER,
            gameOver = false,
        )

        viewModelScope.launch {
            gameRepository.createNewGame(newGame)
                .onSuccess {
                    _uiState.update { currentState ->
                        currentState.copy(
                            userPlayer = Player.X,
                            selectedGame = newGame
                        )
                    }
                    observeOnlineGame(newGame.gameId!!)
                }
                .onFailure { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = exception.message)
                    }
                }
        }
    }

    /**
     * Starts observing available online games for matchmaking.
     */
    private fun startObservingAvailableGames(){
        gameRepository.observeAvailableGames(
            onInitialData = { games ->
                _uiState.update { currentState ->
                    currentState.copy(availableGames = games)
                }
            },
            onGameAdded = { game ->
                _uiState.update { currentState ->
                    currentState.copy(
                        availableGames = currentState.availableGames + game
                    )
                }
            },
            onGameRemoved = { gameId ->
                _uiState.update { currentState ->
                    currentState.copy(
                        availableGames = currentState.availableGames.filter { it.gameId != gameId }
                    )
                }
            },
            onError = { exception ->
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = exception.message)
                }
            }
        )
    }

    /**
     * Stops observing available online games.
     */
    private fun stopObservingAvailableGames(){
        gameRepository.stopObservingAvailableGames()
            .onSuccess {
                _uiState.update { currentState ->
                    currentState.copy(
                        availableGames = emptyList()
                    )
                }
            }
            .onFailure { exception ->
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = exception.message)
                }
            }
    }

    /**
     * Selects an online game from the available games.
     * @param gameId The ID of the game to select.
     */
    private fun selectOnlineGame(gameId: String) {
        viewModelScope.launch {
            gameRepository.selectGame(gameId)
                .onSuccess { game ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            userPlayer = Player.O,
                            selectedGame = game
                        )
                    }
                    observeOnlineGame(game.gameId!!)
                }
                .onFailure { exception ->
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = exception.message)
                    }
                }
        }
    }

    /**
     * Starts observing the selected online game for updates.
     * @param gameId The ID of the game to observe.
     */
    private fun observeOnlineGame(gameId: String) {
        gameRepository.observeGame(
            gameId = gameId,
            onGameUpdate = { updatedGame ->
                _uiState.update { state ->
                    state.copy(selectedGame = updatedGame)
                }
                updateScores(updatedGame.gameState)
                emitMediaPlayer(updatedGame.currentPlayer)
            },
            onFailure = { exception ->
                _uiState.update { state ->
                    state.copy(errorMessage = exception.message)
                }
            }
        )
    }

    /**
     * Removes the listener for the selected online game.
     */
    private fun removeOnlineGameListener() {
        if (_uiState.value.gameMode == GameMode.MULTIPLAYER) {
            val gameId = _uiState.value.selectedGame?.gameId ?: return
            gameRepository.removeGameListener(gameId)
                .onSuccess { deleteOnlineGameIfNeeded(gameId) }
                .onFailure { exception ->
                    _uiState.update { state ->
                        state.copy(errorMessage = exception.message)
                    }
                }
        }
    }

    /**
     * Deletes the online game from the repository if certain conditions are met.
     * @param gameId The ID of the game to delete.
     */
    private fun deleteOnlineGameIfNeeded(gameId: String){
        gameRepository.deleteGame(gameId,
            onSuccess = {
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedGame = null
                    )
                }
            },
            onFailure = { exception ->
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = exception.message)
                }
            }
        )
    }

}
