package com.example.reto_8.mappers

import androidx.room.TypeConverter
import com.example.reto_8.model.Classification

class ClassificationConverter {

    @TypeConverter
    fun fromClassification(classification: Classification): String {
        return classification.name
    }

    @TypeConverter
    fun toClassification(classification: String): Classification {
        return Classification.valueOf(classification)
    }

}
