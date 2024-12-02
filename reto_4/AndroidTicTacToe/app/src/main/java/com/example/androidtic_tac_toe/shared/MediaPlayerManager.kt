package com.example.androidtic_tac_toe.shared

import android.content.Context
import android.media.MediaPlayer
import com.example.androidtic_tac_toe.R

class MediaPlayerManager (
    private val context: Context
) {
    private var mediaPlayerHuman: MediaPlayer? = null
    private var mediaPlayerComputer: MediaPlayer? = null

    fun initialize() {
        mediaPlayerHuman = MediaPlayer.create(context, R.raw.human_sound)
        mediaPlayerComputer = MediaPlayer.create(context, R.raw.computer_sound)
    }

    fun release() {
        mediaPlayerHuman?.release()
        mediaPlayerComputer?.release()
        mediaPlayerHuman = null
        mediaPlayerComputer = null
    }

    fun playHumanSound() {
        mediaPlayerHuman?.apply {
            if (!isPlaying) start()
        }
    }

    fun playComputerSound() {
        mediaPlayerComputer?.apply {
            if (!isPlaying) start()
        }
    }

}
