package com.example.androidtic_tac_toe.ui.screens.game.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.androidtic_tac_toe.R
import com.example.androidtic_tac_toe.model.Player
import com.example.androidtic_tac_toe.model.SquareState

/**
 * Composable for displaying the Tic-Tac-Toe game board.
 * It dynamically generates rows and columns for each cell.
 */
@Composable
fun GameBoard(
    userPlayer: Player,
    isGameOver: Boolean,
    currentUser: Player,
    board: List<SquareState>,
    onClickSquare: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val rows = 3
    val columns = 3
    val xPlayerBitmap = ImageBitmap.imageResource(id = R.drawable.x_player)
    val oPlayerBitmap = ImageBitmap.imageResource(id = R.drawable.o_player)
    val backgroundColor = colorScheme.background

    Canvas(modifier = modifier
        .aspectRatio(1f)
        .background(colorScheme.primary)
        .pointerInput(isGameOver, currentUser, board) {
            detectTapGestures { offset ->

                // Determine the selected square
                val cellWidth = size.width / columns
                val cellHeight = size.height / rows
                val column = (offset.x / cellWidth).toInt()
                val row = (offset.y / cellHeight).toInt()
                val location = row * columns + column

                if (!isGameOver && currentUser == userPlayer && board.getOrNull(location)?.enabled == true) {
                    onClickSquare(location)
                }
            }
        }) {

        val width = size.width
        val height = size.height
        val cellWidth = width / columns
        val cellHeight = height / rows
        val lineThickness = 12f

        // Draw horizontal lines
        drawLine(
            color = backgroundColor,
            start = Offset(0f, cellHeight),
            end = Offset(width, cellHeight),
            strokeWidth = lineThickness
        )
        drawLine(
            color = backgroundColor,
            start = Offset(0f, 2 * cellHeight),
            end = Offset(width, 2 * cellHeight),
            strokeWidth = lineThickness
        )

        // Draw vertical lines
        drawLine(
            color = backgroundColor,
            start = Offset(cellWidth, 0f),
            end = Offset(cellWidth, height),
            strokeWidth = lineThickness
        )
        drawLine(
            color = backgroundColor,
            start = Offset(2 * cellWidth, 0f),
            end = Offset(2 * cellWidth, height),
            strokeWidth = lineThickness
        )

        val padding = 40f
        val paddedWidth = (cellWidth - 2 * padding).toInt()
        val paddedHeight = (cellHeight - 2 * padding).toInt()

        // Draw images for each square
        board.forEachIndexed { index, squareState ->
            val column = index % columns
            val row = index / columns

            val left = (column * cellWidth + padding).toInt()
            val top = (row * cellHeight + padding).toInt()

            when (squareState.player) {
                Player.O -> drawImage(
                    image = xPlayerBitmap,
                    dstOffset = IntOffset(left, top),
                    dstSize = IntSize(paddedWidth, paddedHeight),
                    filterQuality = FilterQuality.High
                )
                Player.X -> drawImage(
                    image = oPlayerBitmap,
                    dstOffset = IntOffset(left, top),
                    dstSize = IntSize(paddedWidth, paddedHeight),
                    filterQuality = FilterQuality.High
                )
                else -> Unit
            }
        }
    }
}
