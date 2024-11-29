package com.example.androidtic_tac_toe.ui.game

data class GameUiState(
    val state: GameState = GameState.NO_WINNER,
    val gameMode: GameMode = GameMode.SINGLE_PLAYER,
    val difficultyLevel: DifficultyLevel = DifficultyLevel.EXPERT,
    val board: List<SquareState> = List(9) { SquareState() },
    val currentPlayer: Player = Player.OPEN_SPOT,
    val isGameOver: Boolean = false,
    val numberComputerWins: Int = 0,
    val numberHumanWins: Int = 0,
    val numberTies: Int = 0,
)

data class SquareState(
    var isEnabled: Boolean = true,
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

enum class DifficultyLevel {
    EASY,
    HARDER,
    EXPERT
}
