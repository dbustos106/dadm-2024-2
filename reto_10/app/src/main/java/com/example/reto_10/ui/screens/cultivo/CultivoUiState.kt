package com.example.reto_10.ui.screens.cultivo

import com.google.gson.annotations.SerializedName

data class CultivoUiState(
    val listCultivos: List<Cultivo> = emptyList(),
    val isLoading: Boolean = false,
)

data class Cultivo(
    @SerializedName("ciclo_del_cultivo") val ciclo_del_cultivo: String?,
    @SerializedName("municipio") val municipio: String?,
    @SerializedName("cultivo") val cultivo: String?,
    @SerializedName("periodo") val periodo: String?,
    @SerializedName("departamento") val departamento: String?,
    @SerializedName("provincia") val provincia: String?,
    @SerializedName("desagregaci_n_cultivo") val desagregacion_cultivo: String?,
    @SerializedName("cultivo2") val cultivo2: String?,
    @SerializedName("grupo_cultivo") val grupo_cultivo: String?,
    @SerializedName("subgrupo") val subgrupo: String?,
    @SerializedName("nombre_cient_fico_del_cultivo") val nombre_cientifico: String?,
    @SerializedName("estado_f_sico_del_cultivo") val estado_fisico: String?
)
