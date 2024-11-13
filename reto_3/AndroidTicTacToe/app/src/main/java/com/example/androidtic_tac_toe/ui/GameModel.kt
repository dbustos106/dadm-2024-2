package com.example.androidtic_tac_toe.ui

data class GameUiState(
    val board: List<ButtonState> = List(9) {ButtonState()},
    val currentPlayer: Char = ' ',
    val isGameOver: Boolean = false,
    val numberPlayerWins: Int = 0,
    val numberComputerWins: Int = 0,
    val numberTies: Int = 0,
    val winner: Int = 0,
)

data class ButtonState(
    var isEnabled: Boolean = true,
    var text: Char = OPEN_SPOT,
)
