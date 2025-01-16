package com.example.reto_8.ui.screens.enterpriseList.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ConfirmationDialog(
    question: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(text = "Confirmación") },
        text = { Text(text = question) },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Sí")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onCancel()
                }
            ) {
                Text("No")
            }
        },
        modifier = modifier
    )

}
