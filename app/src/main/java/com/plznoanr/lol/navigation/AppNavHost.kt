package com.plznoanr.lol.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.plznoanr.lol.feature.bookmark.navigation.bookmarkScreen
import com.plznoanr.lol.feature.home.navigation.HomeRoute
import com.plznoanr.lol.feature.home.navigation.homeScreen
import com.plznoanr.lol.feature.search.navigation.searchGraph
import com.plznoanr.lol.feature.setting.navigation.settingScreen
import com.plznoanr.lol.feature.summoner.navigation.summonerScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    onShowSnackbar: suspend  (String) -> Boolean,
    startDestination: String = HomeRoute
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreen()

        searchGraph(
            nestedGraphs = {
                summonerScreen(
                    onBackPressed = navController::popBackStack,
                )
            }
        )

        bookmarkScreen()

        settingScreen()

    }

}