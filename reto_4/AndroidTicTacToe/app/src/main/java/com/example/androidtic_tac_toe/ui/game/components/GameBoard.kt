package com.example.androidtic_tac_toe.ui.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtic_tac_toe.ui.game.ButtonState
import com.example.androidtic_tac_toe.ui.game.Player

/**
 * Composable for displaying the Tic-Tac-Toe game board.
 * It dynamically generates rows and columns for each cell.
 */
@Composable
fun GameBoard(
    board: List<ButtonState>,
    isGameOver: Boolean,
    currentUser: Player,
    onClickButton: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        for (row in 0..2) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
            ) {
                for (col in 0..2) {
                    val location = row * 3 + col
                    GameButton(
                        location = location,
                        currentUser = currentUser,
                        isGameOver = isGameOver,
                        board = board,
                        onClick = onClickButton,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

/**
 * Represents an individual button on the game board.
 * The button's color and functionality change based on the game state.
 */
@Composable
fun GameButton(
    location: Int,
    currentUser: Player,
    isGameOver: Boolean,
    board: List<ButtonState>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonTextColor = when (board[location].player) {
        Player.HUMAN -> colorScheme.onSecondary
        Player.COMPUTER -> colorScheme.onTertiary
        else -> Color.Gray
    }

    val buttonBackgroundColor = when (board[location].isEnabled) {
        true -> colorScheme.secondary
        else -> colorScheme.tertiary
    }

    ElevatedButton (
        onClick = { onClick(location) },
        shape = RoundedCornerShape(8.dp),
        enabled = board[location].isEnabled
                && currentUser == Player.HUMAN
                && !isGameOver,
        modifier = modifier,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 20.dp,
            pressedElevation = 16.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonBackgroundColor,
            contentColor = buttonTextColor,
            disabledContainerColor = buttonBackgroundColor,
            disabledContentColor = buttonTextColor,
        )
    ) {
        Text(text = board[location].player.symbol.toString(), fontSize = 24.sp)
    }
}
