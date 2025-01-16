package com.example.reto_8.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.reto_8.model.Enterprise

@Dao
interface EnterpriseDao {

    @Query("SELECT * FROM enterprises")
    suspend fun getAllEnterprises(): List<Enterprise>

    @Query("SELECT * FROM enterprises WHERE name LIKE '%' || :query || '%' OR classification LIKE '%' || :query || '%'")
    suspend fun searchEnterprises(query: String): List<Enterprise>

    @Insert
    suspend fun insertEnterprise(enterprise: Enterprise): Long

    @Update
    suspend fun updateEnterprise(enterprise: Enterprise)

    @Delete
    suspend fun deleteEnterprise(enterprise: Enterprise)

}
