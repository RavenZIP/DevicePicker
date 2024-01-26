package com.ravenzip.devicepicker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ravenzip.devicepicker.main.CompareScreen
import com.ravenzip.devicepicker.main.FavouritesScreen
import com.ravenzip.devicepicker.main.HomeScreen
import com.ravenzip.devicepicker.main.SearchScreen
import com.ravenzip.devicepicker.main.UserProfileScreen
import com.ravenzip.devicepicker.navigation.root.RootGraph

@Composable
fun HomeScreenNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME
    ) {
        composable(route = BottomBarGraph.HOME) { HomeScreen() }
        composable(route = BottomBarGraph.SEARCH) { SearchScreen() }
        composable(route = BottomBarGraph.FAVOURITES) { FavouritesScreen() }
        composable(route = BottomBarGraph.COMPARE) { CompareScreen() }
        composable(route = BottomBarGraph.USER_PROFILE) { UserProfileScreen() }
    }
}
