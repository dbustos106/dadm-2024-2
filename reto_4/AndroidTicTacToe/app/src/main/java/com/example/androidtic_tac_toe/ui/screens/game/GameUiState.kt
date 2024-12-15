package com.example.androidtic_tac_toe.ui.screens.game

import com.example.androidtic_tac_toe.model.Game
import com.example.androidtic_tac_toe.model.Player
import com.example.androidtic_tac_toe.model.UserPreferences

data class GameUiState (
    val loading: Boolean = true,
    val availableGames: List<Game> = emptyList(),
    val errorMessage: String? = null,
    val userPlayer: Player? = null,
    val gameMode: GameMode? = null,

    val selectedGame: Game? = null,
    val userPreferences: UserPreferences? = null
)

enum class GameMode {
    SINGLE_PLAYER,
    MULTIPLAYER
}
