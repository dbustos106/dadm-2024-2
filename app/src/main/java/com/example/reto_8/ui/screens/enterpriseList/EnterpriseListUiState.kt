package com.example.reto_8.ui.screens.enterpriseList

import com.example.reto_8.model.Enterprise

data class EnterpriseListUiState(
    val isLoading: Boolean = false,
    val enterprises: List<Enterprise> = emptyList()
)
