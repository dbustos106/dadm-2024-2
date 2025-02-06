package com.example.reto_10.ui.screens.cultivo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto_10.data.CultivoRepository
import com.example.reto_10.ui.screens.cultivo.events.CultivoUiEvent
import com.example.reto_10.ui.screens.cultivo.events.CultivoViewModelEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CultivoViewModel @Inject constructor(
    private val cultivoRepository: CultivoRepository
) : ViewModel() {

    // CultivoList UI state
    private val _uiState = MutableStateFlow(CultivoUiState())
    val uiState = _uiState.asStateFlow()

    // EnterpriseList viewModel events
    private val _viewModelEvent = MutableSharedFlow<CultivoViewModelEvent>(replay = 0)
    val viewModelEvent: SharedFlow<CultivoViewModelEvent> = _viewModelEvent.asSharedFlow()

    /**
     * Handle various UI events triggered bt the user.
     * @param event The UI event to handle.
     */
    fun onEvent(event: CultivoUiEvent) {
        when (event) {
            is CultivoUiEvent.Search -> {
                search(event.municipio)
            }
        }
    }

    private fun search(municipio: String){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val listCultivos = cultivoRepository.search(municipio)
            _uiState.update { it.copy(listCultivos = listCultivos) }
            _uiState.update { it.copy(isLoading = true) }
        }
    }

}
