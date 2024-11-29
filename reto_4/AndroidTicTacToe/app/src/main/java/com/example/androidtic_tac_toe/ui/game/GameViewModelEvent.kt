package com.example.androidtic_tac_toe.ui.game

/**
 * Sealed class representing events in the ViewModel.
 */
sealed interface GameViewModelEvent {

    /**
     * Event to play the sound for a human move.
     */
    data object PlayHumanSound : GameViewModelEvent

    /**
     * Event to play the sound for a computer move.
     */
    data object PlayComputerSound : GameViewModelEvent

}
