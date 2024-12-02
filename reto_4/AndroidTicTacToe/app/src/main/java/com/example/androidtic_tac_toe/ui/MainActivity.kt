package com.example.androidtic_tac_toe.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.androidtic_tac_toe.navigation.TicTacToeNavHost
import com.example.androidtic_tac_toe.shared.MediaPlayerManager
import com.example.androidtic_tac_toe.ui.theme.AndroidTicTacToeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var mediaPlayerManager: MediaPlayerManager

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

    override fun onResume() {
        super.onResume()
        mediaPlayerManager.initialize()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerManager.release()
    }

}
