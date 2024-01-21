package com.ravenzip.devicepicker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {
        authNavigationGraph(navController = navController)
        composable(route = Graph.MAIN) { HomeScreenNavGraph(navController = navController) }
    }
}
