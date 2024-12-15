package com.example.androidtic_tac_toe.ui.screens.game.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.androidtic_tac_toe.R
import com.example.androidtic_tac_toe.model.Game

/**
 * Displays a dialog showing the list of available games or a message if none are available.
 * Allows the user to select an existing game or create a new one
 */
@Composable
fun GameLobbyDialog(
    availableGames: List<Game>,
    onGameSelected: (String) -> Unit,
    onCreateNewGameClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Dialog(onDismissRequest = {}) {
        Surface(
            shape = shapes.medium,
            color = colorScheme.surface,
            modifier = modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.title_text_available_games),
                    style = typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (availableGames.isEmpty()) {
                    Text(
                        text = stringResource(R.string.button_text_no_available_games),
                        style = typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                } else {
                    LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                        items(availableGames) { game ->
                            AvailableGameItem(
                                game = game,
                                onSelectGame = { onGameSelected(it) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onCreateNewGameClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.button_text_create_new_game))
                }

            }
        }
    }

}

/**
 * Displays an individual game item in the list, showing its game ID.
 * Allows the user to select the game.
 */
@Composable
fun AvailableGameItem(
    game: Game,
    onSelectGame: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onSelectGame(game.gameId!!) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = shapes.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.text_game_id_label, game.gameId!!),
                style = typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
