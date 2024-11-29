package com.example.androidtic_tac_toe.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.androidtic_tac_toe.MediaResources
import com.example.androidtic_tac_toe.ui.game.GameMode
import com.example.androidtic_tac_toe.ui.game.GameScreen
import com.example.androidtic_tac_toe.ui.home.HomeScreen

@Composable
fun TicTacToeNavHost (
    mediaResources: MediaResources?,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen(
                onClickStartNewGame = { mode ->
                    navController.navigateSingleTopTo("${Game.route}?${Game.MODE_ARG}=${mode}")
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorScheme.background),
            )
        }
        composable(
            route = Game.routeWithArgs,
            arguments = Game.arguments
        ) { backStackEntry ->
            val mode = backStackEntry.arguments?.getString(Game.MODE_ARG)

            val gameMode = GameMode.valueOf(mode?: GameMode.SINGLE_PLAYER.name)
            GameScreen(
                gameMode = gameMode,
                mediaResources = mediaResources,
                onClickReturnHome = {
                    navController.navigateSingleTopTo(Home.route)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorScheme.background)
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route) {
    popUpTo(
        this@navigateSingleTopTo.graph.findStartDestination().id
    ) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}