package com.example.androidtic_tac_toe.shared

import android.content.Context
import android.media.MediaPlayer
import com.example.androidtic_tac_toe.R

class MediaPlayerManager (
    private val context: Context
) {
    private var mediaPlayerX: MediaPlayer? = null
    private var mediaPlayerO: MediaPlayer? = null

    fun initialize() {
        mediaPlayerX = MediaPlayer.create(context, R.raw.human_sound)
        mediaPlayerO = MediaPlayer.create(context, R.raw.computer_sound)
    }

    fun release() {
        mediaPlayerX?.release()
        mediaPlayerO?.release()
        mediaPlayerX = null
        mediaPlayerO = null
    }

    fun playXSound() {
        mediaPlayerX?.apply {
            if (!isPlaying) start()
        }
    }

    fun playOSound() {
        mediaPlayerO?.apply {
            if (!isPlaying) start()
        }
    }

}
