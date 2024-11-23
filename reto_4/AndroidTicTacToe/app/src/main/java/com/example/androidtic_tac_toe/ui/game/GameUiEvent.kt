package com.example.androidtic_tac_toe.ui.game

/**
 * Sealed class representing UI events for the game screen.
 */
sealed interface GameUiEvent {

    /**
     * Event indicating that the user has selected a new game.
     */
    data object StartNewGame: GameUiEvent

    /**
     * Event indicating that the user has changed the difficulty level.
     * @param difficultyLevel: Selected difficulty level.
     */
    data class ChangeDifficultyLevel(val difficultyLevel: DifficultyLevel): GameUiEvent

    /**
     * Event indicating that the human makes a move by selecting a location on the board.
     * @param location The index of the location on the board.
     */
    data class MakeHumanMove(val location: Int): GameUiEvent

}