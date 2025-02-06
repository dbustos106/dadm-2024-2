package com.example.reto_10.data

import com.example.reto_10.ui.screens.cultivo.Cultivo
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface CultivoApi {
    @Headers("X-App-Token: Fq31t4OAvYoRFzCzyBUEmnd8A")
    @GET("sn28-aa8q.json")
    suspend fun getCultivosPorMunicipio(
        @Query("municipio") municipio: String
    ): List<Cultivo>
}

@Singleton
class CultivoRepository @Inject constructor(
    private val cultivoApi: CultivoApi
) {

    suspend fun search(municipio: String): List<Cultivo> {
        return try {
            cultivoApi.getCultivosPorMunicipio(municipio)
        } catch (e: Exception) {
            emptyList()
        }
    }

}
