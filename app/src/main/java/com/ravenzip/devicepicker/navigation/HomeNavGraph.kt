package com.ravenzip.devicepicker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun HomeScreenNavGraph(navController: NavHostController) {
    NavHost(navController = navController, route = Graph.MAIN, startDestination = "") {}
}
