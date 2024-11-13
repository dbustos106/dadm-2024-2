package com.example.androidtic_tac_toe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

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
     * Starts a new game, setting a random starting player.
     * If the computer goes first, it makes an initial move.
     */
    fun startNewGame(){
        val currentStats = _uiState.value

        _uiState.value = GameUiState(
            currentPlayer = selectRandomPlayer(),
            numberHumanWins = currentStats.numberHumanWins,
            numberComputerWins = currentStats.numberComputerWins,
            numberTies = currentStats.numberTies
        )

        // Make computer move if it’s its turn
        if(_uiState.value.currentPlayer == COMPUTER_PLAYER){
            viewModelScope.launch {
                delay(2000L)
                makeComputerMove()

                // Update the message based on the game result
                val winner = checkForWinner()
                updateGameState(winner)
            }
        }
    }

    /**
     * Selects a random player to start the game.
     */
    private fun selectRandomPlayer(): Char {
        if(Random.nextInt(2) == 0){
            return HUMAN_PLAYER
        }
        return COMPUTER_PLAYER
    }

    /**
     * Determines and executes the computer's move, with
     * blocking logic if the human player is about to win.
     */
    private fun makeComputerMove(){
        var blockingMove: Int? = null
        val board = _uiState.value.board.toMutableList()

        for (i in 0 until BOARD_SIZE) {
            if (board[i].text == OPEN_SPOT) {

                // See if there's a move O can make to win
                board[i] = board[i].copy(text = COMPUTER_PLAYER)
                if (checkForWinner(board) == WINNER_COMPUTER) {
                    setMove(COMPUTER_PLAYER, i)
                    return
                }
                board[i] = board[i].copy(text = OPEN_SPOT)

                // Add logic to block player
                board[i] = board[i].copy(text = HUMAN_PLAYER)
                if (checkForWinner(board) == WINNER_HUMAN) {
                    blockingMove = i
                }
                board[i] = board[i].copy(text = OPEN_SPOT)

            }
        }

        val location = blockingMove ?: (0 until BOARD_SIZE).filter { board[it].text == OPEN_SPOT }.random()
        setMove(COMPUTER_PLAYER, location)
    }

    /**
     * Handles the human player's move, checks for a winner, and
     * initiates the computer's turn if the game is ongoing.
     */
    fun handlePlayerTurn(location: Int){
        setMove(HUMAN_PLAYER, location)

        // Update the message based on the game result
        var winner = checkForWinner()
        updateGameState(winner)

        // Make computer move if the game is not over
        if (winner == 0 && !_uiState.value.isGameOver) {
            viewModelScope.launch {
                delay(2000L)
                makeComputerMove()

                // Update the message based on the game result
                winner = checkForWinner()
                updateGameState(winner)
            }
        }
    }

    /**
     * Checks for a winner based on the current state of
     * the board, including possible tie scenarios.
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

    /**
     * Updates the board with the player's move at the specified location.
     */
    private fun setMove(player: Char, location: Int) {
        val currentBoard = _uiState.value.board.toMutableList()
        currentBoard[location] = currentBoard[location].copy(
            text = player,
            isEnabled = false,
        )

        _uiState.update { currentState ->
            currentState.copy(board = currentBoard)
        }
    }

    /**
     * Updates game state based on the winner, updating
     * stats like wins, ties, and game status.
     */
    private fun updateGameState(winner: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentPlayer = if (_uiState.value.currentPlayer == 'X') 'O' else 'X',
                isGameOver = (winner == TIE || winner == WINNER_HUMAN || winner == WINNER_COMPUTER),
                numberHumanWins = if (winner == WINNER_HUMAN) currentState.numberHumanWins + 1 else currentState.numberHumanWins,
                numberComputerWins = if (winner == WINNER_COMPUTER) currentState.numberComputerWins + 1 else currentState.numberComputerWins,
                numberTies = if (winner == TIE) currentState.numberTies + 1 else currentState.numberTies,
                winner = winner,
            )
        }
    }

}
