package com.example.reto_8.ui.screens.enterpriseList.events

sealed interface EnterpriseListViewModelEvent {
    data class Success(val message: String) : EnterpriseListViewModelEvent
    data class Error(val message: String) : EnterpriseListViewModelEvent
}
