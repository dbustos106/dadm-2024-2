package com.example.androidtic_tac_toe.shared

import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import com.example.androidtic_tac_toe.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SharedViewModel : ViewModel() {

    private val _soundState = MutableStateFlow(SoundState())

    fun initializePlayers(context: Context) {
        val humanSoundPlayer = MediaPlayer.create(context, R.raw.human_sound)
        val computerSoundPlayer = MediaPlayer.create(context, R.raw.computer_sound)

        _soundState.update { currentState ->
            currentState.copy(
                humanSoundPlayer = humanSoundPlayer,
                computerSoundPlayer = computerSoundPlayer
            )
        }
    }

    fun releasePlayers() {
        _soundState.value.humanSoundPlayer?.release()
        _soundState.value.computerSoundPlayer?.release()

        _soundState.update { currentState ->
            currentState.copy(
                humanSoundPlayer = null,
                computerSoundPlayer = null
            )
        }
    }

    fun playHumanSound() {
        _soundState.value.humanSoundPlayer?.apply {
            if (!isPlaying) start()
        }
    }

    fun playComputerSound() {
        _soundState.value.computerSoundPlayer?.apply {
            if (!isPlaying) start()
        }
    }

}