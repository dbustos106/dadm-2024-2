package com.example.reto_10.ui.screens.cultivo.events

sealed interface CultivoViewModelEvent {
    data class Success(val message: String) : CultivoViewModelEvent
    data class Error(val message: String) : CultivoViewModelEvent
}
