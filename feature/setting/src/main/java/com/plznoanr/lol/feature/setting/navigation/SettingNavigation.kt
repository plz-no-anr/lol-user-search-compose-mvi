package com.plznoanr.lol.feature.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.plznoanr.lol.feature.setting.SettingRoute

const val SettingRoute = "setting_route"

fun NavGraphBuilder.settingScreen(
) {
    composable(route = SettingRoute) {
        SettingRoute()
    }
}