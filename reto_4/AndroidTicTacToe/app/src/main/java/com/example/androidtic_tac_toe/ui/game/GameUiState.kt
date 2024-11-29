package com.example.androidtic_tac_toe.ui.game

import androidx.compose.ui.graphics.painter.Painter

data class GameUiState(
    val state: GameState = GameState.NO_WINNER,
    val gameMode: GameMode = GameMode.SINGLE_PLAYER,
    val difficultyLevel: DifficultyLevel = DifficultyLevel.EXPERT,
    val board: List<SquareState> = List(9) { SquareState() },
    val currentPlayer: Player = Player.OPEN_SPOT,
    val isGameOver: Boolean = false,
    val numberHumanWins: Int = 0,
    val numberComputerWins: Int = 0,
    val numberTies: Int = 0,
)

data class SquareState(
    var isEnabled: Boolean = true,
    var player: Player = Player.OPEN_SPOT,
)

data class MenuOption(
    val id: Int,
    val name: String,
    val image: Painter?,
)

enum class Player(val symbol: Char){
    HUMAN('X'),
    COMPUTER('O'),
    OPEN_SPOT(' ')
}

enum class GameState {
    NO_WINNER,
    WINNER_HUMAN,
    WINNER_COMPUTER,
    TIE
}

enum class DifficultyLevel {
    EASY,
    HARDER,
    EXPERT
}

enum class GameMode {
    SINGLE_PLAYER, MULTIPLAYER
}
