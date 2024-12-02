package com.example.androidtic_tac_toe.ui.screens.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androidtic_tac_toe.R

/**
 * Displays the game history scores, including wins for the human,
 * wins for the computer, and the number of ties.
 */
@Composable
fun ScoreSection(
    numberTies: Int,
    numberHumanWins: Int,
    numberComputerWins: Int,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        ScoreItem(
            score = numberHumanWins,
            label = stringResource(R.string.text_human_score),
            Modifier.weight(1f)
        )
        ScoreItem(
            score = numberComputerWins,
            label = stringResource(R.string.text_android_score),
            Modifier.weight(1f)
        )
        ScoreItem(
            score = numberTies,
            label = stringResource(R.string.text_tie_score),
            Modifier.weight(1f)
        )
    }
}

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
        Text(
            text = label,
            style = typography.bodyLarge,
            color = colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = score.toString(),
            style = typography.bodyLarge,
            color = colorScheme.onSurfaceVariant
        )
    }
}
