package com.example.reto_9.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.reto_9.ui.screen.components.rememberMapViewWithLifecycle
import com.example.reto_9.ui.screen.event.MapUiEvent
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by mapViewModel.uiState.collectAsState()
    var radius by rememberSaveable { mutableFloatStateOf(0f) }
    var currentLocation by remember { mutableStateOf<GeoPoint?>(null) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle(context)

    LaunchedEffect(Unit) {
        radius = mapViewModel.loadUserPreferences()
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (!isGranted) Toast.makeText(
                context,
                "Permiso de ubicación denegado",
                Toast.LENGTH_SHORT
            ).show()
        }
    )

    LaunchedEffect(Unit) {
        when {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("MapScreen", "Permiso de ubicación ya concedido")
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Map View
        AndroidView(
            factory = { mapView },
            modifier = Modifier.weight(1f)
        ) { map ->
            // Configuración del mapa
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.zoomController.setZoomInEnabled(false)
            map.setMultiTouchControls(true)
            map.getLocalVisibleRect(Rect())
            map.overlays.clear()

            // Configuración de la ubicación
            val mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), map)
            mMyLocationOverlay.enableMyLocation()
            mMyLocationOverlay.enableFollowLocation()
            mMyLocationOverlay.isDrawAccuracyEnabled = false
            map.overlays.add(mMyLocationOverlay)

            val controller: IMapController = map.controller
            controller.setZoom(16.0)

            mMyLocationOverlay.runOnFirstFix {
                map.post {
                    if (currentLocation == null) {
                        controller.setCenter(mMyLocationOverlay.myLocation)
                        controller.animateTo(mMyLocationOverlay.myLocation)
                        mapViewModel.onEvent(MapUiEvent.UpdateUserLocation(mMyLocationOverlay.myLocation))
                        currentLocation = mMyLocationOverlay.myLocation
                    }
                }
            }

            uiState.pointsOfInterest.forEach { poi ->
                val marker = org.osmdroid.views.overlay.Marker(map)
                marker.position = GeoPoint(poi.mLocation.latitude, poi.mLocation.longitude)
                marker.title = poi.mType
                marker.snippet =
                    "Descripción: ${poi.mDescription ?: "No disponible"} \n Dirección: ${poi.mLocation ?: "No disponible"} \n Ranking: ${poi.mRank}"
                map.overlays.add(marker)
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = radius.toString(),
                onValueChange = { newValue ->
                    val newRadius = newValue.toFloatOrNull()
                    if (newRadius != null) {
                        radius = newRadius
                    }
                },
                label = { Text("Radio de búsqueda (km)") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .focusRequester(focusRequester)
            )

            // Botón para buscar
            Button(
                onClick = {
                    focusManager.clearFocus()
                    mapViewModel.onEvent(
                        MapUiEvent.FetchNearbyPointsOfInterest(
                            uiState.currentLocation,
                            radius
                        )
                    )
                }
            ) {
                Text(text = "Buscar")
            }
        }

    }

}
