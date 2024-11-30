package com.example.androidtic_tac_toe.ui.screens.home.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.androidtic_tac_toe.R

/**
 * Composable function representing the exit confirmation dialog.
 * It asks the user for confirmation before closing the app and provides options to confirm or cancel.
 */
@Composable
fun ExitConfirmationDialog(
    onExitConfirmed: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(text = stringResource(R.string.text_title_exit)) },
        text = { Text(text = stringResource(R.string.text_confirm_exit))},
        confirmButton = {
            Button (onClick = onExitConfirmed) {
                Text(stringResource(R.string.text_option_yes))
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text(stringResource(R.string.text_option_no))
            }
        },
        modifier = modifier
    )
}
