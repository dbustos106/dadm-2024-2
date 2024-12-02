package com.example.androidtic_tac_toe.ui.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.example.androidtic_tac_toe.R

/**
 * Composable function representing the "About" dialog.
 * It shows information about the game and allows the user to dismiss the dialog.
 */
@Composable
fun AboutDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.text_option_about),
                style = MaterialTheme.typography.headlineMedium,
            )
        },
        text = {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(R.drawable.about),
                    contentDescription = stringResource(R.string.description_about_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(shapes.large)
                )
                Text(
                    text = stringResource(R.string.text_about),
                    style = typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            Button (onClick = onDismiss) {
                Text(stringResource(R.string.text_option_ok))
            }
        },
        modifier = modifier
    )
}
