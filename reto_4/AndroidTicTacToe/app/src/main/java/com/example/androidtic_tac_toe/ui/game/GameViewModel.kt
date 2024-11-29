package com.example.androidtic_tac_toe.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

const val BOARD_SIZE = 9

/**
 * ViewModel for managing Tic-Tac-Toe game logic and UI state updates.
 */
class GameViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    /**
     * Initializes a new game and sets the first player.
     */
    init {
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
            is GameUiEvent.ChangeDifficultyLevel -> {
                setDifficultyLevel(event.difficultyLevel)
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
                state = GameState.NO_WINNER,
                board = List(9) { SquareState() },
                currentPlayer = selectRandomPlayer(),
                isGameOver = false,
            )
        }

        // Make computer move if it’s its turn
        if(_uiState.value.currentPlayer == Player.COMPUTER) {
            viewModelScope.launch {
                delay(2000L)
                makeComputerMove()
            }
        }
    }

    /**
     * Sets the game's difficulty level.
     * @param level The difficulty level to set (e.g., EASY, MEDIUM, EXPERT).
     */
    private fun setDifficultyLevel(level: DifficultyLevel) {
        _uiState.update { currentState ->
            currentState.copy(difficultyLevel = level)
        }
    }

    /**
     * Handles the human player's move, checks for a winner, and
     * initiates the computer's turn if the game is ongoing.
     * @param location The index on the game board (0 to 8)
     */
    private fun makeHumanMove(location: Int) {
        setMove(Player.HUMAN, location)

        // Make computer move if the game is not over
        if(!_uiState.value.isGameOver) {
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
    private fun checkGameState(board: List<SquareState> = _uiState.value.board): GameState {
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
        val currentBoard = _uiState.value.board.toMutableList()
        currentBoard[location] = currentBoard[location].copy (
            player = player,
            isEnabled = false,
        )

        _uiState.update { currentState ->
            currentState.copy(board = currentBoard)
        }

        // Update the game state based on the move
        val state = checkGameState()
        updateGameState(state)
    }

    /**
     * Updates game state based on the state, updating stats like wins, ties, and game status.
     * @param state The current state of the game, which is an instance of the `GameState` enum.
     */
    private fun updateGameState(state: GameState) {
        _uiState.update { currentState ->
            currentState.copy (
                state = state,
                currentPlayer = if (_uiState.value.currentPlayer == Player.HUMAN) Player.COMPUTER else Player.HUMAN,
                isGameOver = (state == GameState.TIE || state == GameState.WINNER_HUMAN || state == GameState.WINNER_COMPUTER),
                numberHumanWins = if (state == GameState.WINNER_HUMAN) currentState.numberHumanWins + 1 else currentState.numberHumanWins,
                numberComputerWins = if (state == GameState.WINNER_COMPUTER) currentState.numberComputerWins + 1 else currentState.numberComputerWins,
                numberTies = if (state == GameState.TIE) currentState.numberTies + 1 else currentState.numberTies,
            )
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

}
