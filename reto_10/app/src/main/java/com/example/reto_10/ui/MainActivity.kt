package com.example.reto_10.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.reto_10.ui.screens.cultivo.CultivoScreen
import com.example.reto_10.ui.theme.Reto_10Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Reto_10Theme {
                CultivoScreen()
            }
        }
    }
}
