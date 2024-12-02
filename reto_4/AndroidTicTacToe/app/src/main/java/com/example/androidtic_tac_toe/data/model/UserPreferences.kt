package com.example.androidtic_tac_toe.data.model

data class UserPreferences (
    val difficultyLevel: DifficultyLevel,
    val soundEnabled: Boolean,
    val numberComputerWins: Int,
    val numberHumanWins: Int,
    val numberTies: Int,
)

enum class DifficultyLevel {
    EASY,
    HARDER,
    EXPERT
}
