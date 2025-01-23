package com.example.reto_9.ui.screen.event

import org.osmdroid.util.GeoPoint

sealed interface MapUiEvent {
    data class FetchNearbyPointsOfInterest(val currentLocation: GeoPoint, val radius: Float): MapUiEvent
    data class UpdateUserLocation(val currentLocation: GeoPoint): MapUiEvent
}
