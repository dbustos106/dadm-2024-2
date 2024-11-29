package com.example.androidtic_tac_toe.ui.game.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
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
import com.example.androidtic_tac_toe.ui.game.Player

/**
 * Displays a section with the computer's image and thinking indicator.
 * Visible when it is the computer's turn.
 */
@Composable
fun ComputerSection(
    isGameOver: Boolean,
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
            contentDescription = stringResource(R.string.imagen_de_android),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(shapes.large)
        )

        if(currentPlayer == Player.COMPUTER && !isGameOver) {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .padding(20.dp)
                    .clip(shapes.large)
                    .background(colorScheme.surfaceVariant)
            ) {
                Text(
                    text = stringResource(R.string.android_est_pensando),
                    color = colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }
        }

    }
}
