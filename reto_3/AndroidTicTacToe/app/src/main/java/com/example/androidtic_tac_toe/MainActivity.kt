package com.example.androidtic_tac_toe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtic_tac_toe.ui.theme.AndroidTicTacToeTheme

class MainActivity : ComponentActivity() {

    private lateinit var mGame: TicTacToeGame
    private lateinit var mButtonStates: MutableState<CharArray>
    private lateinit var mCurrentPlayer: MutableState<Char>
    private lateinit var mInfoText: MutableState<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the TicTacToeGame instance
        mGame = TicTacToeGame()

        // Initialize the states
        mButtonStates = mutableStateOf(CharArray(9) { ' ' } )
        mInfoText = mutableStateOf("Player X's turn")
        mCurrentPlayer = mutableStateOf('X')

        setContent {
            AndroidTicTacToeTheme {
                Scaffold(
                    //topBar = { TopAppBar(title = { Text("Mi App") }) },
                    /*floatingActionButton = {
                        FloatingActionButton(onClick = { /* Acción */ }) {
                            Icon(Icons.Filled.Add, contentDescription = "Add")
                        }
                    },*/
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    TicTacToeScreen(
                        game = mGame,
                        buttonStates = mButtonStates,
                        currentPlayer = mCurrentPlayer,
                        infoText = mInfoText,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun TicTacToeScreen(
    game: TicTacToeGame,
    buttonStates: MutableState<CharArray>,
    currentPlayer: MutableState<Char>,
    infoText: MutableState<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {

        // Game board
        TicTacToeBoard(
            game = game,
            buttonStates = buttonStates,
            currentPlayer = currentPlayer,
            infoText = infoText,
            modifier = modifier
        )

        Spacer(modifier = modifier.height(16.dp))

        // Informative text
        Text(
            text = infoText.value,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
    }
}

@Composable
fun TicTacToeBoard(
    game: TicTacToeGame,
    buttonStates: MutableState<CharArray>,
    currentPlayer: MutableState<Char>,
    infoText: MutableState<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {
        for (row in 0..2) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (col in 0..2) {
                    val index = row * 3 + col
                    TicTacToeButton(
                        index = index,
                        buttonStates = buttonStates,
                        currentPlayer = currentPlayer,
                        infoText = infoText,
                        modifier =  modifier,
                    )
                }
            }
        }
    }
}

@Composable
fun TicTacToeButton(
    index: Int,
    buttonStates: MutableState<CharArray>,
    currentPlayer: MutableState<Char>,
    infoText: MutableState<String>,
    modifier: Modifier = Modifier
) {
    ElevatedButton (
        onClick = {
            if (buttonStates.value[index] == ' ') {
                buttonStates.value[index] = currentPlayer.value
                currentPlayer.value = if (currentPlayer.value == 'X') 'O' else 'X'
                infoText.value = "Player ${currentPlayer.value}'s turn"
            }
        },
        modifier = modifier
            .size(100.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 20.dp,
            pressedElevation = 16.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray,
            contentColor = Color.White
        )
    ) {
        Text(text = buttonStates.value[index].toString(), fontSize = 24.sp)
    }
}
