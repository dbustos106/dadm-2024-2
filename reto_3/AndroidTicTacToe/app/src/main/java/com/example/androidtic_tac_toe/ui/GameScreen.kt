package com.example.androidtic_tac_toe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidtic_tac_toe.R
import com.example.androidtic_tac_toe.ui.theme.AndroidTicTacToeTheme
import com.example.androidtic_tac_toe.ui.theme.veryDarkGray

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel(), modifier: Modifier = Modifier) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.primary)
                .weight(1f)
        ) {

            val opacity = if (gameUiState.currentPlayer == HUMAN_PLAYER) 0.4f else 1f

            GameComputerSection(
                currentPlayer = gameUiState.currentPlayer,
                computerMessage = gameUiState.computerMessage,
                modifier = Modifier
                    .padding(start = 30.dp)
                    .alpha(opacity)
            )

            GameBoard(
                board = gameUiState.board,
                onClick = { location -> gameViewModel.handlePlayerTurn(location) },
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = gameUiState.infoText,
                fontSize = 18.sp,
                color = colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 30.dp)
            )
        }

        GameBar(
            numberTies = gameUiState.numberTies,
            numberPlayerWins = gameUiState.numberPlayerWins,
            numberComputerWins = gameUiState.numberComputerWins,
            modifier = Modifier
                .fillMaxWidth()
                .background(veryDarkGray)
        )
    }
}

@Composable
fun GameComputerSection(
    currentPlayer: Char,
    computerMessage: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        Image(
            painter = painterResource(R.drawable.computer),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        if(currentPlayer == COMPUTER_PLAYER) {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .padding(20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.onPrimary)
            ) {
                Text(
                    text = computerMessage,
                    color = colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }
        }

    }
}

@Composable
fun GameBoard(
    board: List<ButtonState>,
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
                        board = board,
                        onClick = onClick,
                        location = location,
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

@Composable
fun GameButton(
    board: List<ButtonState>,
    onClick: (Int) -> Unit,
    location: Int,
    modifier: Modifier = Modifier
) {
    ElevatedButton (
        onClick = { onClick(location) },
        shape = RoundedCornerShape(8.dp),
        enabled = board[location].isEnabled,
        modifier = modifier,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 20.dp,
            pressedElevation = 16.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = board[location].backgroundColor,
            contentColor = board[location].textColor
        )
    ) {
        Text(text = board[location].text.toString(), fontSize = 24.sp)
    }
}

@Composable
fun GameBar(
    numberTies: Int,
    numberPlayerWins: Int,
    numberComputerWins: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
    ) {
        GameBarItem(score = numberPlayerWins, label = stringResource(R.string.tu), Modifier.weight(1f))
        GameBarItem(score = numberComputerWins, label = stringResource(R.string.computador), Modifier.weight(1f))
        GameBarItem(score = numberTies, label = stringResource(R.string.empates), Modifier.weight(1f))
    }
}

@Composable
fun GameBarItem(score: Int, label: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = score.toString(), color = colorScheme.onPrimary)
        Text(text = label, color = colorScheme.onPrimary)
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    AndroidTicTacToeTheme {
        GameScreen()
    }
}