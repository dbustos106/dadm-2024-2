package com.example.androidtic_tac_toe.ui.screens.game.events

import com.example.androidtic_tac_toe.model.DifficultyLevel
import com.example.androidtic_tac_toe.ui.screens.game.GameMode

/**
 * Sealed class representing UI events for the game screen.
 */
sealed interface GameUiEvent {

    // general functions
    data class SetGameMode(val gameMode: GameMode): GameUiEvent
    data object StartNewLocalGame: GameUiEvent
    data class MakePlayerMove(val location: Int): GameUiEvent
    data class SetDifficultyLevel(val difficultyLevel: DifficultyLevel): GameUiEvent
    data class SetSoundEnabled(val soundEnabled: Boolean): GameUiEvent
    data object ResetScores: GameUiEvent

    // Online functions
    data object StartNewOnlineGame: GameUiEvent
    data object StartObservingAvailableGames: GameUiEvent
    data object StopObservingAvailableGames: GameUiEvent
    data class SelectOnlineGame(val gameId: String): GameUiEvent
    data object DeleteOnlineGameIfNeeded: GameUiEvent

}
