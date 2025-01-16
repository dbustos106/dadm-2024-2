package com.example.reto_8.ui.screens.enterpriseList.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reto_8.model.Classification
import com.example.reto_8.model.Enterprise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEnterpriseDialog(
    onCreateClick: (Enterprise) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var websiteUrl by rememberSaveable { mutableStateOf("") }
    var contactPhone by rememberSaveable { mutableStateOf("") }
    var contactEmail by rememberSaveable { mutableStateOf("") }
    var productsAndServices by rememberSaveable { mutableStateOf("") }

    var expandedDropdownMenu by rememberSaveable { mutableStateOf(false) }
    var selectedClassification by rememberSaveable { mutableStateOf(Classification.CONSULTING) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Crear nueva empresa") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la empresa") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = websiteUrl,
                    onValueChange = { websiteUrl = it },
                    label = { Text("Sitio web") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = contactPhone,
                    onValueChange = { contactPhone = it },
                    label = { Text("Teléfono de contacto") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = contactEmail,
                    onValueChange = { contactEmail = it },
                    label = { Text("Correo de contacto") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = productsAndServices,
                    onValueChange = { productsAndServices = it },
                    label = { Text("Productos y servicios") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedDropdownMenu,
                    onExpandedChange = { expandedDropdownMenu = !expandedDropdownMenu },
                ) {
                    TextField(
                        value = selectedClassification.name,
                        onValueChange = {},
                        label = { Text("Clasificación") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdownMenu)
                        },
                        readOnly = true,
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropdownMenu,
                        onDismissRequest = { expandedDropdownMenu = false }
                    ) {
                        Classification.entries.forEach { classification ->
                            DropdownMenuItem(
                                text = { Text(text = classification.name) },
                                onClick = {
                                    selectedClassification = classification
                                    expandedDropdownMenu = false
                                }
                            )
                        }
                    }
                }

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val enterprise = Enterprise(
                        name = name,
                        websiteUrl = websiteUrl,
                        contactPhone = contactPhone,
                        contactEmail = contactEmail,
                        productsAndServices = productsAndServices,
                        classification = selectedClassification
                    )

                    onCreateClick(enterprise)
                    onDismiss()
                }
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        modifier = modifier
    )
}
