package com.example.androidtic_tac_toe.ui.screens.game.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.androidtic_tac_toe.R

/**
 * Composable that displays the game bar.
 * Requires the use of experimental Material3 APIs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    soundEnabled: Boolean,
    options: List<MenuOption>,
    onToggleSound: (Boolean) -> Unit,
    onReturnHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showOptionsMenu by rememberSaveable{ mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = { onReturnHomeClick() }) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = stringResource(R.string.description_return_home),
                )
            }
        },
        actions = {
            IconButton(onClick = { onToggleSound(!soundEnabled) }) {
                val icon = if (soundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeMute
                Icon(icon, contentDescription = stringResource(R.string.description_option_toggle_sound))
            }
            IconButton(onClick = { showOptionsMenu = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.description_menu_options),
                )

                OptionsMenu (
                    options = options,
                    expanded = showOptionsMenu,
                    onDismiss = { showOptionsMenu = false },
                    modifier = Modifier
                )
            }
        },
        modifier = modifier
    )
}
