package com.example.androidtic_tac_toe.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.androidtic_tac_toe.ui.theme.computerPlayerButtonColor
import com.example.androidtic_tac_toe.ui.theme.humanPlayerButtonColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

/**
 * Represents the game logic for Tic-Tac-Toe (3x3). Manages the game board,
 * player moves, and checks for a winner. Players are 'X' (human) and
 * 'O' (computer), with empty spots represented by a space (' ').
 */
class GameViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    /**
     * Initializes the game by starting a new game session.
     */
    init {
        startNewGame()
    }

    /**
     * Re-initializes a new game session.
     */
    fun startNewGame(){
        _uiState.value = GameUiState(currentPlayer = selectRandomPlayer())
        updateMessages(0)

        // Make computer move if it’s its turn
        if(_uiState.value.currentPlayer == COMPUTER_PLAYER){
            viewModelScope.launch {
                delay(1000L)
                makeComputerMove()

                // Update the message based on the game result
                val winner = checkForWinner()
                updateGameState(winner)
                updateMessages(winner)
            }
        }
    }

    /**
     * Selects a random player to start the game.
     * @return `HUMAN_PLAYER` if the human is selected to
     * start, or `COMPUTER_PLAYER` if the computer is selected.
     */
    private fun selectRandomPlayer(): Char {
        if(Random.nextInt(2) == 0){
            return HUMAN_PLAYER
        }
        return COMPUTER_PLAYER
    }

    /**

     * Executes the computer's move in the game.
     */
    private fun makeComputerMove(){
        var blockingMove: Int? = null
        val board = _uiState.value.board.toMutableList()

        for (i in 0 until BOARD_SIZE) {
            if (board[i].text == OPEN_SPOT) {

                // See if there's a move O can make to win
                board[i] = board[i].copy(text = COMPUTER_PLAYER)
                if (checkForWinner(board) == 3) {
                    setMove(COMPUTER_PLAYER, i)
                }
                board[i] = board[i].copy(text = OPEN_SPOT)

                // Add logic to block player
                board[i] = board[i].copy(text = HUMAN_PLAYER)
                if (checkForWinner(board) == 2) {
                    blockingMove = i
                }
                board[i] = board[i].copy(text = OPEN_SPOT)

            }
        }

        val location = blockingMove ?: (0 until BOARD_SIZE).filter { board[it].text == OPEN_SPOT }.random()
        setMove(COMPUTER_PLAYER, location)
    }

    /**
     * Handles the player's move at the specified location.
     */
    fun handlePlayerTurn(location: Int){
        setMove(HUMAN_PLAYER, location)

        // Update the message based on the game result
        var winner = checkForWinner()
        updateGameState(winner)
        updateMessages(winner)

        // Make computer move if the game is not over
        if (winner == 0 && !_uiState.value.isGameOver) {
            viewModelScope.launch {
                delay(1000L)
                makeComputerMove()

                // Update the message based on the game result
                winner = checkForWinner()
                updateGameState(winner)
                updateMessages(winner)
            }
        }
    }

    /**
     * Updates the game state based on the game result.
     * @param winner An integer representing the game's outcome:
     * 0 if no winner or tie yet, 1 if it's a tie, 2 if X won, or 3 if O won.
     */
    private fun updateGameState(winner: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                isGameOver = (winner == 2 || winner == 3),
                currentPlayer = if (_uiState.value.currentPlayer == 'X') 'Y' else 'X',
            )
        }
    }

    /**
     * Updates the game messages based on the game outcome.
     */
    private fun updateMessages(winner: Int){
        _uiState.update { currentState ->
            currentState.copy(
                infoText = when (winner) {
                    0 -> {
                        if (_uiState.value.currentPlayer == 'X') "Es tu turno, haz una buena jugada"
                        else "Turno de Android"
                    }
                    1 -> "es un empate"
                    2 -> "has ganado"
                    else -> "android ha ganado"
                },
                computerMessage = "Android está pensando..."
            )
        }
    }

    /**
     * Set the given player at the given location on the game board.
     * The location must be available, or the board will not be changed.
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    private fun setMove(player: Char, location: Int) {
        val currentBoard = _uiState.value.board.toMutableList()
        currentBoard[location] = currentBoard[location].copy(
            text = player,
            isEnabled = false,
            textColor = if (player == HUMAN_PLAYER) humanPlayerButtonColor
                        else computerPlayerButtonColor,
            backgroundColor = Color.DarkGray
        )

        _uiState.update { currentState ->
            currentState.copy(
                board = currentBoard
            )
        }
    }

    /**
     * Check for a winner and return a status value indicating who has won.
     * @param board - Tic-Tac-Toe board state
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    private fun checkForWinner(board: List<ButtonState> = _uiState.value.board): Int {

        val winningLines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )

        for (line in winningLines) {
            if (board[line[0]].text == HUMAN_PLAYER &&
                board[line[1]].text == HUMAN_PLAYER &&
                board[line[2]].text == HUMAN_PLAYER
            ) {
                return WINNER_HUMAN
            }
            if (board[line[0]].text == COMPUTER_PLAYER &&
                board[line[1]].text == COMPUTER_PLAYER &&
                board[line[2]].text == COMPUTER_PLAYER
            ) {
                return WINNER_COMPUTER
            }
        }

        // Check for tie
        if (board.all { it.text == HUMAN_PLAYER || it.text == COMPUTER_PLAYER }) {
            return TIE
        }

        // No winner or tie yet
        return NO_WINNER
    }

}
