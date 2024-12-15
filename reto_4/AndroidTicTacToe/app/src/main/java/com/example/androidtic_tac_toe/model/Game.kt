package com.example.androidtic_tac_toe.model

data class Game(
    val gameId: String? = null,
    val isAvailable: Boolean = true,
    val board: List<SquareState> = emptyList(),
    val currentPlayer: Player = Player.OPEN_SPOT,
    val gameState: GameState = GameState.NO_WINNER,
    val gameOver: Boolean = false,
) {
    constructor(
        gameId: String?,
        board: List<SquareState>,
        gameOver: Boolean,
        currentPlayer: Player,
        gameState: GameState
    ) : this(
        gameId = gameId,
        board = board,
        currentPlayer = currentPlayer,
        gameState = gameState,
        isAvailable = true,
        gameOver = gameOver,
    )
}

data class SquareState (
    var enabled: Boolean = true,
    var player: Player = Player.OPEN_SPOT,
)

enum class GameState {
    NO_WINNER,
    WINNER_X,
    WINNER_O,
    TIE
}

enum class Player {
    OPEN_SPOT,
    X,
    O
}
