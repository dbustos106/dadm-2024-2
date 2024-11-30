package com.example.androidtic_tac_toe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.androidtic_tac_toe.navigation.TicTacToeNavHost
import com.example.androidtic_tac_toe.shared.SharedViewModel
import com.example.androidtic_tac_toe.ui.theme.AndroidTicTacToeTheme

class MainActivity : ComponentActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            AndroidTicTacToeTheme {
                TicTacToeNavHost(
                    sharedViewModel = sharedViewModel,
                    navController = navController,
                    modifier = Modifier
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.initializePlayers(applicationContext)
    }

    override fun onPause() {
        super.onPause()
        sharedViewModel.releasePlayers()
    }

}
