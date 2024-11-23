package com.example.androidtic_tac_toe.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Contract for information needed on every Tic-Tac-Toe navigation destination
 */
sealed interface TicTacToeDestinations {
    val route: String
}

/**
 * Tic-Tac-Toe app navigation destinations
 */
data object Home : TicTacToeDestinations {
    override val route = "home"
}

data object Game : TicTacToeDestinations {
    override val route = "game"
    const val MODE_ARG = "mode"
    val routeWithArgs = "game?${MODE_ARG}={${MODE_ARG}}"
    val arguments = listOf(
        navArgument(MODE_ARG) { type = NavType.StringType }
    )
}
