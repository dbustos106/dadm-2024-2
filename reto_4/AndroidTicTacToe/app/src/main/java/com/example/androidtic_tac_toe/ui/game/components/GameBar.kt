package com.example.androidtic_tac_toe.ui.game.components

import androidx.compose.material.icons.Icons
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
fun GameBar(
    onClickReturnHome: () -> Unit = {},
    onClickStartNewGame: () -> Unit = {},
    onClickChangeDifficulty: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showOptionsMenu by rememberSaveable{ mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = stringResource(R.string.tic_tac_toe)) },
        navigationIcon = {
            IconButton(onClick = { onClickReturnHome() }) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = stringResource(R.string.regresar_a_home),
                )
            }
        },
        actions = {
            IconButton(onClick = { showOptionsMenu = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.m_s_opciones),
                )

                OptionsMenu (
                    expanded = showOptionsMenu,
                    onItemClick = { option ->
                        when (option.id) {
                            0 -> {
                                onClickStartNewGame()
                                onClickChangeDifficulty()
                            }
                            1 -> {
                                onClickChangeDifficulty()
                            }
                        }
                    },
                    onDismiss = { showOptionsMenu = false },
                    modifier = Modifier
                )
            }
        },
        modifier = modifier
    )
}
