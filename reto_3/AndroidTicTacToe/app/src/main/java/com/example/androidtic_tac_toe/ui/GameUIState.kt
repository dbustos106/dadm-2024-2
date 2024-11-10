package com.example.androidtic_tac_toe.ui

import androidx.compose.ui.graphics.Color
import com.example.androidtic_tac_toe.ui.theme.defaultButtonColor
import com.example.androidtic_tac_toe.ui.theme.defaultButtonTextColor

data class ButtonState(
    var backgroundColor: Color = defaultButtonColor,
    var textColor: Color = defaultButtonTextColor,
    var isEnabled: Boolean = true,
    var text: Char = OPEN_SPOT,
)

data class GameUiState(
    val board: List<ButtonState> = List(9) {ButtonState()},
    val currentPlayer: Char = HUMAN_PLAYER,
    val isGameOver: Boolean = false,
    val numberPlayerWins: Int = 0,
    val numberComputerWins: Int = 0,
    val numberTies: Int = 0,
    val infoText: String = " ",
    val computerMessage: String = " "
)
