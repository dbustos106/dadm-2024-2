package com.example.reto_8.data

import com.example.reto_8.data.local.EnterpriseDao
import com.example.reto_8.model.Enterprise
import com.example.reto_8.model.RoomResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnterpriseRepository @Inject constructor(
    private val enterpriseDao: EnterpriseDao
){

    suspend fun getAllEnterprises(): RoomResponse<List<Enterprise>> {
        return try {
            val enterprises = enterpriseDao.getAllEnterprises()
            RoomResponse.Success(enterprises)
        } catch (e: Exception) {
            RoomResponse.Error("Error al obtener las empresas: ${e.message}")
        }
    }

    suspend fun searchEnterprises(query: String): RoomResponse<List<Enterprise>> {
        return try {
            val enterprises = enterpriseDao.searchEnterprises(query)
            RoomResponse.Success(enterprises)
        } catch (e: Exception) {
            RoomResponse.Error("Error al buscar empresas: ${e.message}")
        }
    }

    suspend fun insertEnterprise(enterprise: Enterprise): RoomResponse<Enterprise> {
        return try {
            val id = enterpriseDao.insertEnterprise(enterprise)
            RoomResponse.Success(enterprise.copy(id = id))
        } catch (e: Exception) {
            RoomResponse.Error("Error al insertar la empresa: ${e.message}")
        }
    }

    suspend fun updateEnterprise(enterprise: Enterprise): RoomResponse<Unit> {
        return try {
            enterpriseDao.updateEnterprise(enterprise)
            RoomResponse.Success(Unit)
        } catch (e: Exception) {
            RoomResponse.Error("Error al actualizar la empresa: ${e.message}")
        }
    }

    suspend fun deleteEnterprise(enterprise: Enterprise): RoomResponse<Unit> {
        return try {
            enterpriseDao.deleteEnterprise(enterprise)
            RoomResponse.Success(Unit)
        } catch (e: Exception) {
            RoomResponse.Error("Error al eliminar la empresa: ${e.message}")
        }
    }

}
