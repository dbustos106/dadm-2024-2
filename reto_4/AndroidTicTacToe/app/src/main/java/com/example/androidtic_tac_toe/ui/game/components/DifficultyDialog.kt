package com.example.androidtic_tac_toe.ui.game.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.androidtic_tac_toe.R
import com.example.androidtic_tac_toe.ui.game.DifficultyLevel

/**
 * Displays a dialog for selecting the difficulty level.
 */
@Composable
fun DifficultyDialog(
    onDismiss: () -> Unit,
    onDifficultySelected: (DifficultyLevel) -> Unit,
    currentDifficultyLevel: DifficultyLevel,
    modifier: Modifier = Modifier
){
    var selectedDifficulty by rememberSaveable { mutableStateOf(currentDifficultyLevel) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.text_title_difficulty)) },
        text = {
            Column {
                DifficultyLevel.entries.forEach { difficultyLevel ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedDifficulty = difficultyLevel }
                            .padding(vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = selectedDifficulty == difficultyLevel,
                            onClick = { selectedDifficulty = difficultyLevel }
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        val difficultyLevelText = when (difficultyLevel) {
                            DifficultyLevel.EASY -> stringResource(R.string.text_option_easy)
                            DifficultyLevel.HARDER -> stringResource(R.string.text_option_harder)
                            DifficultyLevel.EXPERT -> stringResource(R.string.text_option_expert)
                        }

                        Text(text = difficultyLevelText)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                    onDifficultySelected(selectedDifficulty)
                    onDismiss()
            }) {
                Text(text = stringResource(R.string.text_option_ok))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(R.string.text_option_close))
            }
        },
        modifier = modifier
    )
}
