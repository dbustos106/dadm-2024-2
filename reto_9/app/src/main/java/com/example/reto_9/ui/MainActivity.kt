package com.example.reto_9.ui

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.reto_9.R
import com.example.reto_9.ui.screen.MapScreen
import com.example.reto_9.ui.theme.Reto_9Theme
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        )

        setContent {
            Reto_9Theme {
                MapScreen()
            }
        }
    }
}
