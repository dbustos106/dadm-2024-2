package com.example.reto_8.ui.screens.enterpriseList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reto_8.model.Classification
import com.example.reto_8.model.Enterprise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterpriseCard(
    enterprise: Enterprise,
    onUpdateClick: (Enterprise) -> Unit,
    onDeleteClick: (Enterprise) -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    var expandedDropdownMenu by rememberSaveable { mutableStateOf(false) }

    var isEditing by rememberSaveable { mutableStateOf(false) }
    var editedEnterprise by remember { mutableStateOf(enterprise) }
    var selectedClassification by rememberSaveable { mutableStateOf(editedEnterprise.classification) }

    if (showConfirmationDialog) {
        ConfirmationDialog(
            question = "¿Estás seguro de que deseas eliminar esta empresa?",
            onConfirm = {
                onDeleteClick(enterprise)
                showConfirmationDialog = false
            },
            onCancel = { showConfirmationDialog = false }
        )
    }

    Card(
        modifier = modifier.padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isEditing) {
                TextField(
                    value = editedEnterprise.name,
                    onValueChange = { editedEnterprise = editedEnterprise.copy(name = it) },
                    label = { Text("Nombre de la empresa") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedEnterprise.websiteUrl,
                    onValueChange = { editedEnterprise = editedEnterprise.copy(websiteUrl = it) },
                    label = { Text("URL de la página web") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedEnterprise.contactPhone,
                    onValueChange = { editedEnterprise = editedEnterprise.copy(contactPhone = it) },
                    label = { Text("Teléfono de contacto") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedEnterprise.contactEmail,
                    onValueChange = { editedEnterprise = editedEnterprise.copy(contactEmail = it) },
                    label = { Text("Email de contacto") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedEnterprise.productsAndServices,
                    onValueChange = { editedEnterprise = editedEnterprise.copy(productsAndServices = it) },
                    label = { Text("Productos y servicios") },
                    modifier = Modifier.fillMaxWidth()
                )
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
                                    editedEnterprise = editedEnterprise.copy(classification = selectedClassification)
                                    expandedDropdownMenu = false
                                }
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        onUpdateClick(editedEnterprise)
                        isEditing = false
                    }) {
                        Text("Guardar")
                    }
                    Button(onClick = {
                        isEditing = false
                        editedEnterprise = enterprise
                        selectedClassification = enterprise.classification
                    }) {
                        Text("Cancelar")
                    }
                }
            } else {
                Text(text = "Nombre: ${enterprise.name}")
                Text(text = "URL: ${enterprise.websiteUrl}")
                Text(text = "Teléfono: ${enterprise.contactPhone}")
                Text(text = "Email: ${enterprise.contactEmail}")
                Text(text = "Productos y servicios: ${enterprise.productsAndServices}")
                Text(text = "Clasificación: ${enterprise.classification.name}")

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { showConfirmationDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }

}
