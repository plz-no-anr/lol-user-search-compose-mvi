package com.plznoanr.lol_usersearch_compose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.plznoanr.lol_usersearch_compose.ui.navigation.destination.MainDestination
import com.plznoanr.lol_usersearch_compose.ui.navigation.destination.SearchDestination
import com.plznoanr.lol_usersearch_compose.ui.navigation.destination.SpectatorDestination
import com.plznoanr.lol_usersearch_compose.ui.navigation.destination.SummonerDestination

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Main.route,
    ) {
        mainDestination(navController = navController)

        searchDestination(navController = navController)

        summonerDestination(navController = navController)

        spectatorDestination(navController = navController)

    }

}

fun NavGraphBuilder.mainDestination(
    navController: NavController,
) {
    composable(route = Destination.Main.route) {
        MainDestination(navController = navController)
    }
}

fun NavGraphBuilder.searchDestination(
    navController: NavController,
) {
    composable(route = Destination.Search.route) {
        SearchDestination(navController = navController)
    }
}

fun NavGraphBuilder.summonerDestination(
    navController: NavController,
) {
    composable(
        route = Destination.Summoner.route,
        arguments = listOf(navArgument(name = Destination.Summoner.Args.KEY_SUMMONER_NAME) {
            type = NavType.StringType
        })
    ) {
        SummonerDestination(navController = navController)
    }
}

fun NavGraphBuilder.spectatorDestination(
    navController: NavController,
) {
    composable(
        route = Destination.Spectator.route,
        arguments = listOf(navArgument(name = Destination.Spectator.Args.KEY_SUMMONER_NAME) {
            type = NavType.StringType
        })
    ) {
        SpectatorDestination(navController = navController)
    }
}

fun NavController.navigateToMain() {
    navigate(route = Destination.Main.route)
}

fun NavController.navigateToSearch() {
    navigate(route = Destination.Search.route)
}

fun NavController.navigateToSpectator(name: String) {
    navigate(route = Destination.Spectator.pathWithArgs(name))
}

fun NavController.navigateToSummoner(name: String) {
    navigate(route = Destination.Summoner.routeWithArgs(name))
}