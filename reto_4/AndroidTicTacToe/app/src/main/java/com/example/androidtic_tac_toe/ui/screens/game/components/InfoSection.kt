package com.example.androidtic_tac_toe.ui.screens.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.androidtic_tac_toe.R
import com.example.androidtic_tac_toe.model.GameState
import com.example.androidtic_tac_toe.model.Player
import com.example.androidtic_tac_toe.ui.screens.game.GameMode

/**
 * Displays a section with the computer's image and thinking indicator.
 * Visible when it is the computer's turn.
 */
@Composable
fun InfoSection(
    userPlayer: Player,
    gameMode: GameMode,
    gameState: GameState,
    currentPlayer: Player,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        Image(
            painter = when(gameMode) {
                GameMode.SINGLE_PLAYER -> painterResource(R.drawable.computer_section)
                else -> painterResource(R.drawable.default_avatar)
            },
            contentDescription = stringResource(R.string.description_computer_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(shapes.large)
        )

        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .clip(shapes.large)
                .background(colorScheme.surfaceVariant)
        ) {
            val infoText = when (gameState) {
                GameState.TIE -> stringResource(R.string.state_text_tie)
                GameState.WINNER_X -> {
                    if(userPlayer == Player.X) stringResource(R.string.state_text_won)
                    else stringResource(R.string.state_text_loss)
                }
                GameState.WINNER_O -> {
                    if(userPlayer == Player.O) stringResource(R.string.state_text_won)
                    else stringResource(R.string.state_text_loss)
                }
                else -> {
                    if(currentPlayer == userPlayer) stringResource (R.string.state_text_your_turn)
                    else stringResource(R.string.state_text_not_your_turn)
                }
            }

            Text(
                text = infoText,
                color = colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start,
                style = typography.bodyLarge,
                maxLines = Int.MAX_VALUE,
                softWrap = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }
    }

}
