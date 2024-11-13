package com.example.androidtic_tac_toe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.androidtic_tac_toe.ui.GameScreen
import com.example.androidtic_tac_toe.ui.theme.AndroidTicTacToeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTicTacToeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    innerPadding -> GameScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(colorScheme.background)
                    )
                }
            }
        }
    }

}
