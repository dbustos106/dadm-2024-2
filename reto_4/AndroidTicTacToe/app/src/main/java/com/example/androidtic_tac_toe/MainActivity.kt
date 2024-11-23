package com.example.androidtic_tac_toe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.androidtic_tac_toe.navigation.TicTacToeNavHost
import com.example.androidtic_tac_toe.ui.theme.AndroidTicTacToeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AndroidTicTacToeTheme {
                TicTacToeNavHost(
                    navController = navController,
                    modifier = Modifier
                )
            }
        }
    }

}
