package com.example.androidtic_tac_toe.ui

import androidx.lifecycle.ViewModel
import com.example.androidtic_tac_toe.ui.theme.computerPlayerButtonColor
import com.example.androidtic_tac_toe.ui.theme.humanPlayerButtonColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

/**
 * Represents the game logic for Tic-Tac-Toe (3x3).
 * Manages the game board, player moves, and checks for a winner.
 * Players are 'X' (human) and 'O' (computer), with empty spots represented by a space (' ').
 *
 */
class GameViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        startNewGame()
    }

    /**
     * Re-initializes the game data to restart the game.
     */
    fun startNewGame(){
        _uiState.value = GameUiState(currentPlayer = selectRandomPlayer())

    }

    private fun selectRandomPlayer(): Char {
        if(Random.nextInt(2) == 0){
            return HUMAN_PLAYER
        }
        return COMPUTER_PLAYER
    }

    fun makeComputerMove(){

    }

    fun handlePlayerTurn(location: Int){
        /*if (mButtonStates.value[location].isEnabled) {
            setMove(GameViewModel.HUMAN_PLAYER, location)

            // If there is no winner yet, let the computer make a move
            var winner = mGame.checkForWinner()
            if (winner == 0) {
                mInfoText.value = getString(R.string.it_s_android_s_turn)
                val move = mGame.getComputerMove()
                setMove(GameViewModel.COMPUTER_PLAYER, move)
                winner = mGame.checkForWinner()
            }

            // Update the message based on the game result
            mInfoText.value = when (winner) {
                0 -> getString(R.string.it_s_your_turn)
                1 -> getString(R.string.it_s_a_tie)
                2 -> getString(R.string.you_won)
                else -> getString(R.string.android_won)
            }
        }*/
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
            backgroundColor = if (player == HUMAN_PLAYER) humanPlayerButtonColor else computerPlayerButtonColor
        )
        _uiState.update { it.copy(board = currentBoard)}
    }

    /**
     * Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
    private fun getBetterComputerMove(): Int {
        var blockingMove: Int? = null
        val board = _uiState.value.board.toMutableList()

        for (i in 0 until BOARD_SIZE) {
            if (board[i].text == OPEN_SPOT) {

                // See if there's a move O can make to win
                board[i] = board[i].copy(text = COMPUTER_PLAYER)
                if (checkForWinner(board) == 3) {
                    return i
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

        return blockingMove ?: (0 until BOARD_SIZE).filter { board[it].text == OPEN_SPOT }.random()
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
