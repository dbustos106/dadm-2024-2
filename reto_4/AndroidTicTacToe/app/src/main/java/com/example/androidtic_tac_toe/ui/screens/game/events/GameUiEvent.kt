package com.example.androidtic_tac_toe.ui.screens.game.events

import com.example.androidtic_tac_toe.data.model.DifficultyLevel

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
    data class SetDifficultyLevel(val difficultyLevel: DifficultyLevel): GameUiEvent

    /**
     * Event indicating that the user toggles sound settings.
     * @param soundEnabled Indicates whether the sound is enabled or disabled.
     */
    data class SetSoundEnabled(val soundEnabled: Boolean): GameUiEvent

    /**
     * Event indicating that the human makes a move by selecting a location on the board.
     * @param location The index of the location on the board.
     */
    data class MakeHumanMove(val location: Int): GameUiEvent

}
