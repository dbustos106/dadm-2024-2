package com.example.reto_10.ui.screens.cultivo.events

sealed interface CultivoUiEvent {
    data class Search(val municipio: String): CultivoUiEvent
}
