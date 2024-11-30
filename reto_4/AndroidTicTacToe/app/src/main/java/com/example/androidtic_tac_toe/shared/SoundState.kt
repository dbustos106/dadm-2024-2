package com.example.androidtic_tac_toe.shared

import android.media.MediaPlayer

data class SoundState (
    val humanSoundPlayer: MediaPlayer? = null,
    val computerSoundPlayer: MediaPlayer? = null,
)