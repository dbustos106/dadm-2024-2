package com.example.androidtic_tac_toe

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.androidtic_tac_toe.navigation.TicTacToeNavHost
import com.example.androidtic_tac_toe.ui.theme.AndroidTicTacToeTheme

data class MediaResources(
    val humanSoundPlayer: MediaPlayer,
    val computerSoundPlayer: MediaPlayer
)

class MainActivity : ComponentActivity() {

    private var mediaResources: MediaResources? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AndroidTicTacToeTheme {
                TicTacToeNavHost(
                    mediaResources = mediaResources,
                    navController = navController,
                    modifier = Modifier
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val humanSoundPlayer = MediaPlayer.create(this, R.raw.human_sound)
        val computerSoundPlayer = MediaPlayer.create(this, R.raw.computer_sound)
        mediaResources = MediaResources(humanSoundPlayer, computerSoundPlayer)
    }

    override fun onPause() {
        super.onPause()
        mediaResources?.humanSoundPlayer?.release()
        mediaResources?.computerSoundPlayer?.release()
        mediaResources = null
    }

}
