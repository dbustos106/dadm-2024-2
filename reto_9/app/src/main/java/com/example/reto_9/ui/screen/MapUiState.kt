package com.example.reto_9.ui.screen

import org.osmdroid.bonuspack.location.POI
import org.osmdroid.util.GeoPoint

data class MapUiState(
    val errorMessage: String? = "",
    val currentLocation: GeoPoint = GeoPoint(0.0, 0.0),
    val pointsOfInterest: List<POI> = emptyList()
)
