package com.example.androidtic_tac_toe.ui.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Shows an individual score item with a label and value.
 * Used in the game score summary.
 */
@Composable
fun ScoreItem(
    score: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = score.toString(), color = colorScheme.onSurfaceVariant)
        Text(text = label, color = colorScheme.onSurfaceVariant)
    }
}