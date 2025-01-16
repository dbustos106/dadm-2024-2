package com.example.reto_8.ui.screens.enterpriseList

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reto_8.ui.screens.enterpriseList.components.EnterpriseCard
import com.example.reto_8.ui.screens.enterpriseList.components.NewEnterpriseDialog
import com.example.reto_8.ui.screens.enterpriseList.events.EnterpriseListUiEvent
import com.example.reto_8.ui.screens.enterpriseList.events.EnterpriseListViewModelEvent

@Composable
fun EnterpriseListScreen(
    enterpriseListViewModel: EnterpriseListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by enterpriseListViewModel.uiState.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showNewEnterpriseDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        enterpriseListViewModel.onEvent(EnterpriseListUiEvent.GetEnterprises)
    }

    LaunchedEffect(enterpriseListViewModel.viewModelEvent) {
        enterpriseListViewModel.viewModelEvent.collect { event ->
            when (event) {
                is EnterpriseListViewModelEvent.Success -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is EnterpriseListViewModelEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewEnterpriseDialog = true },
                content = { Icon(Icons.Default.Add, contentDescription = "Agregar empresa") }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { newQuery ->
                    searchQuery = newQuery
                    enterpriseListViewModel.onEvent(
                        EnterpriseListUiEvent.SearchEnterprises(
                            searchQuery = newQuery
                        )
                    )
                },
                label = { Text("Buscar nombre o clasificación") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Log.d("Devu", "${uiState.enterprises}")

                items(uiState.enterprises, key = { enterprise -> enterprise.id }) { enterprise ->
                    EnterpriseCard(
                        enterprise = enterprise,
                        onUpdateClick = { newEnterprise ->
                            enterpriseListViewModel.onEvent(
                                EnterpriseListUiEvent.UpdateEnterprise(
                                    newEnterprise
                                )
                            )
                        },
                        onDeleteClick = {
                            enterpriseListViewModel.onEvent(
                                EnterpriseListUiEvent.DeleteEnterprise(
                                    enterprise
                                )
                            )
                        },
                        modifier = Modifier
                    )
                }
            }
        }
    }

    if (showNewEnterpriseDialog) {
        NewEnterpriseDialog(
            onCreateClick = { newEnterprise ->
                enterpriseListViewModel.onEvent(EnterpriseListUiEvent.InsertEnterprise(newEnterprise))
            },
            onDismiss = { showNewEnterpriseDialog = false }
        )
    }

}
