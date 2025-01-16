package com.example.reto_8.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.reto_8.mappers.ClassificationConverter

@Entity(tableName = "enterprises")
@TypeConverters(ClassificationConverter::class)
data class Enterprise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val websiteUrl: String = "",
    val contactPhone: String = "",
    val contactEmail: String = "",
    val productsAndServices: String = "",
    val classification: Classification = Classification.CONSULTING
)

enum class Classification {
    CONSULTING,
    CUSTOM_DEVELOPMENT,
    SOFTWARE_FACTORY
}
