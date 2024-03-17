package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.screens.main.CompareScreen
import com.ravenzip.devicepicker.screens.main.FavouritesScreen
import com.ravenzip.devicepicker.screens.main.HomeScreen
import com.ravenzip.devicepicker.screens.main.SearchScreen
import com.ravenzip.devicepicker.screens.main.UserProfileScreen
import com.ravenzip.devicepicker.services.DataService

@Composable
fun HomeScreenNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    dataService: DataService
) {
    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME
    ) {
        composable(route = BottomBarGraph.HOME) {
            HomeScreen(padding = padding, dataService = dataService)
        }
        composable(route = BottomBarGraph.SEARCH) { SearchScreen(padding) }
        composable(route = BottomBarGraph.FAVOURITES) { FavouritesScreen(padding) }
        composable(route = BottomBarGraph.COMPARE) { CompareScreen(padding) }
        composable(route = BottomBarGraph.USER_PROFILE) { UserProfileScreen(padding) }
    }
}
