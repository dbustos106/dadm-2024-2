package com.example.androidtic_tac_toe.ui

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidtic_tac_toe.R
import com.example.androidtic_tac_toe.ui.theme.AndroidTicTacToeTheme

/**
 *
 */
@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel(), modifier: Modifier = Modifier) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    val openOptionsDialog = remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { openOptionsDialog.value = !openOptionsDialog.value },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.opciones)
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val opacity = if (gameUiState.currentPlayer == HUMAN_PLAYER) 0.4f else 1f
            GameComputerSection(
                currentPlayer = gameUiState.currentPlayer,
                modifier = Modifier
                    .padding(start = 30.dp)
                    .alpha(opacity)
            )

            GameBoard(
                board = gameUiState.board,
                isGameOver = gameUiState.isGameOver,
                currentUser = gameUiState.currentPlayer,
                onClick = { location -> gameViewModel.handlePlayerTurn(location) },
                modifier = Modifier.padding(16.dp)
            )

            val infoText = when (gameUiState.winner) {
                0 -> {
                    if (gameUiState.currentPlayer == 'X') stringResource(R.string.es_tu_turno_haz_una_buena_jugada)
                    else stringResource(R.string.turno_de_android)
                }
                1 -> stringResource(R.string.es_un_empate)
                2 -> stringResource(R.string.has_ganado)
                else -> stringResource(R.string.android_ha_ganado)
            }

            Text(
                text = infoText,
                fontSize = 18.sp,
                color = colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 30.dp)
            )

            if(gameUiState.isGameOver || openOptionsDialog.value){
                FinalOptionsDialog(
                    numberTies = gameUiState.numberTies,
                    numberPlayerWins = gameUiState.numberPlayerWins,
                    numberComputerWins = gameUiState.numberComputerWins,
                    onPlayAgain = { gameViewModel.startNewGame() }
                )
            }
        }
    }
}

/**
 *
 */
@Composable
fun GameComputerSection(
    currentPlayer: Char,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        Image(
            painter = painterResource(R.drawable.computer),
            contentDescription = stringResource(R.string.imagen_de_android),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(shapes.large)
        )

        if(currentPlayer == COMPUTER_PLAYER) {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .padding(20.dp)
                    .clip(shapes.large)
                    .background(colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.android_est_pensando),
                    color = colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }
        }

    }
}

/**
 *
 */
@Composable
fun GameBoard(
    board: List<ButtonState>,
    isGameOver: Boolean,
    currentUser: Char,
    onClick: (Int) -> Unit,
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
                        onClick = onClick,
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
 *
 */
@Composable
fun GameButton(
    location: Int,
    currentUser: Char,
    isGameOver: Boolean,
    board: List<ButtonState>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val buttonTextColor = when (board[location].text) {
        'X' -> colorScheme.onSecondary
        'O' -> colorScheme.onTertiary
        else -> Color.Gray
    }

    val buttonBackgroundColor = when (board[location].isEnabled) {
        false -> colorScheme.surfaceVariant
        else -> colorScheme.surface
    }

    ElevatedButton (
        onClick = { onClick(location) },
        shape = RoundedCornerShape(8.dp),
        enabled = board[location].isEnabled
                && currentUser == HUMAN_PLAYER
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
        Text(text = board[location].text.toString(), fontSize = 24.sp)
    }
}

/**
 *
 */
@Composable
private fun FinalOptionsDialog(
    numberTies: Int,
    numberPlayerWins: Int,
    numberComputerWins: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.opciones)) },
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
            ) {
                GameScoreItem(
                    score = numberPlayerWins,
                    label = stringResource(R.string.tu),
                    Modifier.weight(1f)
                )
                GameScoreItem(
                    score = numberComputerWins,
                    label = stringResource(R.string.android),
                    Modifier.weight(1f)
                )
                GameScoreItem(
                    score = numberTies,
                    label = stringResource(R.string.empates),
                    Modifier.weight(1f)
                )
            }
               },

        modifier = modifier,
        dismissButton = {
            TextButton(onClick = { activity.finish() }) {
                Text(text = stringResource(R.string.cerrar))
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(R.string.nuevo_juego))
            }
        },
        containerColor = colorScheme.surface
    )
}

/**
 *
 */
@Composable
fun GameScoreItem(score: Int, label: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = score.toString(), color = colorScheme.onSurface)
        Text(text = label, color = colorScheme.onSurface)
    }
}

/**
 *
 */
@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    AndroidTicTacToeTheme {
        GameScreen()
    }
}
