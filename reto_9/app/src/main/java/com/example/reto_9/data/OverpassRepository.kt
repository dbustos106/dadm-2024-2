package com.example.reto_9.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.bonuspack.location.OverpassAPIProvider
import org.osmdroid.bonuspack.location.POI
import org.osmdroid.util.GeoPoint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverpassRepository @Inject constructor(){

    suspend fun fetchNearbyPOIs(center: GeoPoint, radius: Float): Result<List<POI>> {
        return try {
            // Configuración de la consulta en Overpass
            val provider = OverpassAPIProvider()
            val query = buildOverpassQuery(center, radius)

            // Ejecutar la consulta en un hilo de trabajo
            val pois: ArrayList<POI> = withContext(Dispatchers.IO) {
                provider.getPOIsFromUrl(query)
            }

            // Retornar el resultado con los POIs encontrados
            Result.success(pois)
        } catch (e: Exception) {
            // Retornar el error si la consulta falla
            Result.failure(e)
        }
    }

    private fun buildOverpassQuery(center: GeoPoint, radius: Float): String {
        val lat = center.latitude
        val lon = center.longitude
        val radiusInMeters = radius * 1000

        // Consulta para obtener nodos con etiquetas de interés
        val query = """
            [out:json];
            (
              node["amenity"](around:$radiusInMeters,$lat,$lon);
              node["tourism"](around:$radiusInMeters,$lat,$lon);
            );
            out center;
        """.trimIndent()

        return "https://overpass-api.de/api/interpreter?data=${query.replace("\n", "").replace(" ", "%20")}"
    }

}
