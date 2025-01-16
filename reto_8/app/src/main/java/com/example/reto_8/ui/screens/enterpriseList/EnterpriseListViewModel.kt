package com.example.reto_8.ui.screens.enterpriseList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto_8.data.EnterpriseRepository
import com.example.reto_8.model.Enterprise
import com.example.reto_8.model.RoomResponse
import com.example.reto_8.ui.screens.enterpriseList.events.EnterpriseListUiEvent
import com.example.reto_8.ui.screens.enterpriseList.events.EnterpriseListViewModelEvent
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
class EnterpriseListViewModel @Inject constructor(
    private val enterpriseRepository: EnterpriseRepository
) : ViewModel() {

    // EnterpriseList UI state
    private val _uiState = MutableStateFlow(EnterpriseListUiState())
    val uiState = _uiState.asStateFlow()

    // EnterpriseList viewModel events
    private val _viewModelEvent = MutableSharedFlow<EnterpriseListViewModelEvent>(replay = 0)
    val viewModelEvent: SharedFlow<EnterpriseListViewModelEvent> = _viewModelEvent.asSharedFlow()

    /**
     * Handle various UI events triggered bt the user.
     * @param event The UI event to handle.
     */
    fun onEvent(event: EnterpriseListUiEvent) {
        when(event) {
            is EnterpriseListUiEvent.GetEnterprises ->{
                getEnterprises()
            }
            is EnterpriseListUiEvent.SearchEnterprises -> {
                searchEnterprises(event.searchQuery)
            }
            is EnterpriseListUiEvent.InsertEnterprise -> {
                insertEnterprise(event.enterprise)
            }
            is EnterpriseListUiEvent.UpdateEnterprise -> {
                updateEnterprise(event.newEnterprise)
            }
            is EnterpriseListUiEvent.DeleteEnterprise -> {
                deleteEnterprise(event.enterprise)
            }
        }
    }

    private fun getEnterprises(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val response = enterpriseRepository.getAllEnterprises()) {
                is RoomResponse.Success -> {
                    _uiState.update { it.copy(enterprises = response.data) }
                }
                is RoomResponse.Error -> {
                    _viewModelEvent.emit(EnterpriseListViewModelEvent.Error(response.errorMessage))
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun searchEnterprises(searchQuery: String){
        viewModelScope.launch {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                when (val response = enterpriseRepository.searchEnterprises(searchQuery)) {
                    is RoomResponse.Success -> {
                        _uiState.update { it.copy(enterprises = response.data) }
                    }
                    is RoomResponse.Error -> {
                        _viewModelEvent.emit(EnterpriseListViewModelEvent.Error(response.errorMessage))
                    }
                }

                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun insertEnterprise(enterprise: Enterprise){
        viewModelScope.launch {
            when (val response = enterpriseRepository.insertEnterprise(enterprise)) {
                is RoomResponse.Success -> {
                    _viewModelEvent.emit(EnterpriseListViewModelEvent.Success("Empresa insertada exitosamente"))
                    _uiState.update { it.copy(enterprises = _uiState.value.enterprises + response.data) }
                }
                is RoomResponse.Error -> {
                    _viewModelEvent.emit(EnterpriseListViewModelEvent.Error(response.errorMessage))
                }
            }
        }
    }

    private fun updateEnterprise(newEnterprise: Enterprise){
        viewModelScope.launch {
            when (val response = enterpriseRepository.updateEnterprise(newEnterprise)) {
                is RoomResponse.Success -> {
                    _viewModelEvent.emit(EnterpriseListViewModelEvent.Success("Empresa actualizada exitosamente"))

                    val updatedEnterprises = _uiState.value.enterprises.toMutableList()
                    val index = updatedEnterprises.indexOfFirst { it.id == newEnterprise.id }
                    if (index != -1) { updatedEnterprises[index] = newEnterprise }
                    _uiState.update { it.copy(enterprises = updatedEnterprises) }
                }
                is RoomResponse.Error -> {
                    _viewModelEvent.emit(EnterpriseListViewModelEvent.Error(response.errorMessage))
                }
            }
        }
    }

    private fun deleteEnterprise(enterprise: Enterprise){
        viewModelScope.launch {
            when (val response = enterpriseRepository.deleteEnterprise(enterprise)) {
                is RoomResponse.Success -> {
                    _viewModelEvent.emit(EnterpriseListViewModelEvent.Success("Empresa eliminada exitosamente"))

                    val updatedEnterprises = _uiState.value.enterprises.filter { it.id != enterprise.id }
                    _uiState.update { it.copy(enterprises = updatedEnterprises) }
                }
                is RoomResponse.Error -> {
                    _viewModelEvent.emit(EnterpriseListViewModelEvent.Error(response.errorMessage))
                }
            }
        }
    }

}
