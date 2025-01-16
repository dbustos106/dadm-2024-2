package com.example.reto_8.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.reto_8.ui.screens.enterpriseList.EnterpriseListScreen
import com.example.reto_8.ui.theme.Reto_8Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Reto_8Theme {
                EnterpriseListScreen()
            }
        }
    }
}
