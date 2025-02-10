package com.example.reto_11.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.reto_11.ui.screen.chatbot.ChatbotScreen
import com.example.reto_11.ui.theme.Reto_11Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Reto_11Theme {
                ChatbotScreen()
            }
        }
    }
}
