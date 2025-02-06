package com.example.reto_10.ui.screens.cultivo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reto_10.ui.screens.cultivo.events.CultivoUiEvent

@Composable
fun CultivoScreen(
    cultivoViewModel: CultivoViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by cultivoViewModel.uiState.collectAsState()
    var municipio by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        val customPrimary = Color(0xFF1E88E5)
        val customOnPrimary = Color(0xFFFFFFFF)
        val customOnSurface = Color(0xFF212121)
        val customBorder = Color(0xFFBBDEFB)

        OutlinedTextField(
            value = municipio,
            onValueChange = { municipio = it },
            label = { Text("Ingrese el municipio") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                cursorColor = customPrimary,
                focusedLabelColor = customPrimary,
                unfocusedLabelColor = customOnSurface,
                focusedIndicatorColor = customPrimary,
                unfocusedIndicatorColor = customBorder,
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { cultivoViewModel.onEvent(CultivoUiEvent.Search(municipio)) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = customPrimary,
                contentColor = customOnPrimary
            )
        ) {
            Text("Buscar")
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(uiState.listCultivos) { cultivo ->
                CultivoItem(cultivo)
            }
        }
    }
}

@Composable
fun CultivoItem(cultivo: Cultivo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = cultivo.cultivo ?: "", style = MaterialTheme.typography.titleMedium)
            Text(text = "Municipio: ${cultivo.municipio}", style = MaterialTheme.typography.bodyMedium)

            Text(text = "Ciclo del cultivo: ${cultivo.ciclo_del_cultivo}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Cultivo: ${cultivo.cultivo}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Periodo: ${cultivo.periodo}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Departamento: ${cultivo.departamento}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Provincia: ${cultivo.provincia}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Desagregación del cultivo: ${cultivo.desagregacion_cultivo}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Cultivo2: ${cultivo.cultivo2}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Grupo cultivo: ${cultivo.grupo_cultivo}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Subgrupo: ${cultivo.subgrupo}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Nombre científico: ${cultivo.nombre_cientifico}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Estado físico: ${cultivo.estado_fisico}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
