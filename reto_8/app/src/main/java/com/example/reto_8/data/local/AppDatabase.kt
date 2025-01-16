package com.example.reto_8.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.reto_8.mappers.ClassificationConverter
import com.example.reto_8.model.Enterprise

@Database(entities = [Enterprise::class], version = 2, exportSchema = true)
@TypeConverters(ClassificationConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun enterpriseDao(): EnterpriseDao
}
