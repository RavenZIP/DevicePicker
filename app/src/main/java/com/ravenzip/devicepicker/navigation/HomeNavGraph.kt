package com.ravenzip.devicepicker.navigation

import androidx.compose.foundation.layout.PaddingValues
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
fun HomeScreenNavGraph(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME
    ) {
        composable(route = BottomBarGraph.HOME) { HomeScreen(padding) }
        composable(route = BottomBarGraph.SEARCH) { SearchScreen(padding) }
        composable(route = BottomBarGraph.FAVOURITES) { FavouritesScreen(padding) }
        composable(route = BottomBarGraph.COMPARE) { CompareScreen(padding) }
        composable(route = BottomBarGraph.USER_PROFILE) { UserProfileScreen(padding) }
    }
}
