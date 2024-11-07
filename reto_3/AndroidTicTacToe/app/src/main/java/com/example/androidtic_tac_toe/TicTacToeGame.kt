package com.example.androidtic_tac_toe

import kotlin.random.Random

/**
 * Represents the game logic for Tic-Tac-Toe (3x3).
 * Manages the game board, player moves, and checks for a winner.
 * Players are 'X' (human) and 'O' (computer), with empty spots represented by a space (' ').
 *
 */
class TicTacToeGame {

    // Constants for the game
    companion object {
        const val BOARD_SIZE = 9
        const val HUMAN_PLAYER = 'X'
        const val COMPUTER_PLAYER = 'O'
        const val OPEN_SPOT = ' '
    }

    private val board: CharArray = CharArray(BOARD_SIZE) { OPEN_SPOT }

    /**
     * Clear the board of all X's and O's by setting all spots to OPEN_SPOT.
     */
    fun clearBoard() {
        board.fill(OPEN_SPOT)
    }

    /**
     * Set the given player at the given location on the game board.
     * The location must be available, or the board will not be changed.
     *
     * @param player - The HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-8) to place the move
     */
    fun setMove(player: Char, location: Int) {
        if (location in 0 until BOARD_SIZE && board[location] == OPEN_SPOT) {
            board[location] = player
        }
    }

    /**
     * Return the best move for the computer to make. You must call setMove()
     * to actually make the computer move to that location.
     * @return The best move for the computer to make (0-8).
     */
    fun getComputerMove(): Int {
        // Implement logic to return the best move for the computer
        for (i in 0 until BOARD_SIZE) {
            if (board[i] == OPEN_SPOT) {
                val current = board[i]
                board[i] = COMPUTER_PLAYER
                if (checkForWinner() == 3) {
                    return i
                } else {
                    board[i] = current
                }
            }
        }

        // Add logic to block player (if necessary)
        for (i in 0 until BOARD_SIZE) {
            if (board[i] == OPEN_SPOT) {
                val current = board[i]
                board[i] = HUMAN_PLAYER
                if (checkForWinner() == 2) {
                    board[i] = COMPUTER_PLAYER
                    return i
                } else {
                    board[i] = current
                }
            }
        }

        // Generate random move
        var move: Int
        do {
            move = Random.nextInt(BOARD_SIZE)
        } while (board[move] != OPEN_SPOT)

        return move
    }

    /**
     * Check for a winner and return a status value indicating who has won.
     * @return Return 0 if no winner or tie yet, 1 if it's a tie, 2 if X won,
     * or 3 if O won.
     */
    fun checkForWinner(): Int {
        // Check horizontal wins
        for (i in 0..6 step 3) {
            if (board[i] == HUMAN_PLAYER && board[i + 1] == HUMAN_PLAYER && board[i + 2] == HUMAN_PLAYER) return 2
            if (board[i] == COMPUTER_PLAYER && board[i + 1] == COMPUTER_PLAYER && board[i + 2] == COMPUTER_PLAYER) return 3
        }

        // Check vertical wins
        for (i in 0..2) {
            if (board[i] == HUMAN_PLAYER && board[i + 3] == HUMAN_PLAYER && board[i + 6] == HUMAN_PLAYER) return 2
            if (board[i] == COMPUTER_PLAYER && board[i + 3] == COMPUTER_PLAYER && board[i + 6] == COMPUTER_PLAYER) return 3
        }

        // Check diagonal wins
        if ((board[0] == HUMAN_PLAYER && board[4] == HUMAN_PLAYER && board[8] == HUMAN_PLAYER) ||
            (board[2] == HUMAN_PLAYER && board[4] == HUMAN_PLAYER && board[6] == HUMAN_PLAYER)) return 2
        if ((board[0] == COMPUTER_PLAYER && board[4] == COMPUTER_PLAYER && board[8] == COMPUTER_PLAYER) ||
            (board[2] == COMPUTER_PLAYER && board[4] == COMPUTER_PLAYER && board[6] == COMPUTER_PLAYER)) return 3

        // Check for tie
        if (board.all { it == HUMAN_PLAYER || it == COMPUTER_PLAYER }) return 1

        return 0 // No winner or tie yet
    }
}
