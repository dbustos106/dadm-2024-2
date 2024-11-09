package com.example.androidtic_tac_toe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidtic_tac_toe.R
import com.example.androidtic_tac_toe.ui.theme.AndroidTicTacToeTheme

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel(), modifier: Modifier = Modifier) {
    val gameUiState by gameViewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxSize().background(colorScheme.primary)
    ) {

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(start = 30.dp, top = 20.dp, end = 30.dp)
        ){
            Image(
                painter = painterResource(R.drawable.computer),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.size(80.dp).clip(RoundedCornerShape(16.dp))
            )

            Box(
                modifier = modifier.height(80.dp)
                    .padding(20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.onPrimary)
            ) {
                Text(
                    text = gameUiState.infoText,
                    color = colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = modifier.fillMaxWidth()
                        .align(Alignment.Center)
                )
            }

        }

        GameBoard(
            board = gameUiState.board,
            onClick = { location -> gameViewModel.handlePlayerTurn(location) },
            modifier = modifier
        )

        Spacer(modifier = modifier.height(16.dp))

        Text(text = gameUiState.infoText,
            fontSize = 18.sp,
            color = colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
fun GameBoard(board: List<ButtonState>, onClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        for (row in 0..2) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
            ) {
                for (col in 0..2) {
                    val location = row * 3 + col
                    GameButton(
                        board = board,
                        onClick = onClick,
                        location = location,
                        modifier =  modifier.weight(1f).aspectRatio(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun GameButton(board: List<ButtonState>, onClick: (Int) -> Unit, location: Int, modifier: Modifier = Modifier) {
    ElevatedButton (
        onClick = { onClick(location) },
        shape = RoundedCornerShape(8.dp),
        enabled = board[location].isEnabled,
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
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

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    AndroidTicTacToeTheme {
        GameScreen()
    }
}