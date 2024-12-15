package com.example.androidtic_tac_toe.model

data class UserPreferences (
    val difficultyLevel: DifficultyLevel,
    val soundEnabled: Boolean,
    val numberLosses: Int,
    val numberWins: Int,
    val numberTies: Int,
)

enum class DifficultyLevel {
    EASY,
    HARDER,
    EXPERT
}
