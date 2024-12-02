package com.example.androidtic_tac_toe.ui.screens.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.example.androidtic_tac_toe.ui.screens.game.GameState
import com.example.androidtic_tac_toe.ui.screens.game.Player

/**
 * Displays a section with the computer's image and thinking indicator.
 * Visible when it is the computer's turn.
 */
@Composable
fun ComputerSection(
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
            painter = painterResource(R.drawable.computer_section),
            contentDescription = stringResource(R.string.description_computer_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(shapes.large)
        )

        Box(
            modifier = Modifier
                .padding(20.dp)
                .clip(shapes.large)
                .background(colorScheme.surfaceVariant)
        ) {
            val infoText = when (gameState) {
                GameState.TIE -> stringResource(R.string.text_state_tie)
                GameState.WINNER_HUMAN -> stringResource(R.string.text_state_human_won)
                GameState.WINNER_COMPUTER -> stringResource(R.string.text_state_android_won)
                else -> {
                    if (currentPlayer == Player.COMPUTER) stringResource(R.string.text_state_android_turn)
                    else stringResource(R.string.text_state_human_turn)
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
                    .padding(start = 5.dp, bottom = 10.dp, end = 5.dp, top = 10.dp)
            )
        }
    }

}
