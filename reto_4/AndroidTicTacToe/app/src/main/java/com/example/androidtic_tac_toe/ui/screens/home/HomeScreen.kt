package com.example.androidtic_tac_toe.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.androidtic_tac_toe.R
import com.example.androidtic_tac_toe.ui.screens.game.GameMode
import com.example.androidtic_tac_toe.ui.screens.home.components.AboutDialog

/**
 * Composable function representing the home screen of the app.
 * This screen includes options for playing against the Android, playing with a friend,
 * accessing the "About" dialog, and exiting the app.
 */
@Composable
fun HomeScreen(
    onStartNewGameClick: (GameMode) -> Unit = {},
    modifier: Modifier = Modifier
){
    var showAboutDialog by rememberSaveable { mutableStateOf(false) }

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = typography.headlineMedium,
            color = colorScheme.onBackground
        )

        when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                HomeImage(size = 180.dp)
            }
            else -> {
                HomeImage(size = 80.dp)
            }
        }

        Button (
            onClick = { onStartNewGameClick(GameMode.SINGLE_PLAYER) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 20.dp, end = 20.dp)

        ) {
            Text(text = stringResource(R.string.button_text_game_android))
        }

        Button(
            onClick = { onStartNewGameClick(GameMode.MULTIPLAYER) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 20.dp, end = 20.dp)
        ) {
            Text(text = stringResource(R.string.button_text_online_game))
        }

        Button(
            onClick = { showAboutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 20.dp, end = 20.dp)
        ) {
            Text(text = stringResource(R.string.button_text_about))
        }

    }

    if (showAboutDialog) {
        AboutDialog (
            onDismiss = { showAboutDialog = false },
            modifier = Modifier
        )
    }

}

/**
* Displays an image at the home screen
*/
@Composable
fun HomeImage(
    size: Dp,
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .padding(20.dp)
            .border(
                width = 2.dp,
                color = colorScheme.background,
                shape = CircleShape
            )
            .shadow(
                elevation = 16.dp,
                shape = CircleShape
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.icono_con_fondo),
            contentDescription = stringResource(R.string.description_app_icon),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

}
