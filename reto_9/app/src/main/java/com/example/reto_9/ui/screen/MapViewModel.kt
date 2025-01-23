package com.example.reto_9.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reto_9.data.OverpassRepository
import com.example.reto_9.data.UserPreferencesRepository
import com.example.reto_9.ui.screen.event.MapUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class MapViewModel  @Inject constructor(
    private val overpassRepository: OverpassRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    fun onEvent(event: MapUiEvent) {
        when (event) {
            is MapUiEvent.FetchNearbyPointsOfInterest -> {
                fetchNearbyPointsOfInterest(event.currentLocation, event.radius)
            }
            is MapUiEvent.UpdateUserLocation -> {
                updateUserLocation(event.currentLocation)
            }
        }
    }

    private fun fetchNearbyPointsOfInterest(currentLocation: GeoPoint, radius: Float){
        viewModelScope.launch {
            userPreferencesRepository.updateRadius(radius)
            val result = overpassRepository.fetchNearbyPOIs(currentLocation, radius)
            result.onSuccess { points ->
                _uiState.update { it.copy(pointsOfInterest = points) }
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(errorMessage = error.message)
            }
        }
    }

    private fun updateUserLocation(currentLocation: GeoPoint){
        _uiState.update { it.copy(currentLocation = currentLocation) }
    }

    fun loadUserPreferences(): Float{
        return runBlocking {
            val userPreferences = userPreferencesRepository.userPreferencesFlow.first()
            userPreferences.radius
        }
    }

}
