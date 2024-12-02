package com.example.androidtic_tac_toe.ui.screens.game

import com.example.androidtic_tac_toe.data.model.DifficultyLevel

data class GameUiState (
    val loading: Boolean = true,
    val gameOver: Boolean = false,
    val soundEnabled: Boolean = true,
    val currentPlayer: Player = Player.OPEN_SPOT,
    val gameState: GameState = GameState.NO_WINNER,
    val gameMode: GameMode = GameMode.SINGLE_PLAYER,
    val difficultyLevel: DifficultyLevel = DifficultyLevel.EXPERT,
    val board: List<SquareState> = List(9) { SquareState() },
    val numberComputerWins: Int = 0,
    val numberHumanWins: Int = 0,
    val numberTies: Int = 0,
)

data class SquareState (
    var enabled: Boolean = true,
    var player: Player = Player.OPEN_SPOT,
)

enum class GameMode {
    SINGLE_PLAYER,
    MULTIPLAYER
}

enum class GameState {
    NO_WINNER,
    WINNER_HUMAN,
    WINNER_COMPUTER,
    TIE
}

enum class Player {
    HUMAN,
    COMPUTER,
    OPEN_SPOT
}
