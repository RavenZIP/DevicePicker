package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ravenzip.devicepicker.extensions.functions.navigateWithSlideAnimation
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.ui.screens.main.compare.CompareScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.favourites.FavouritesScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.search.SearchScreenScaffold
import com.ravenzip.devicepicker.viewmodels.main.HomeScreenViewModel
import com.ravenzip.devicepicker.viewmodels.main.SearchScreenViewModel
import com.ravenzip.devicepicker.viewmodels.main.UserProfileViewModel

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    padding: PaddingValues,
    navigateToSplashScreen: () -> Unit,
) {
    val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
    val searchScreenViewModel = hiltViewModel<SearchScreenViewModel>()
    val userProfileViewModel = hiltViewModel<UserProfileViewModel>()

    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME,
    ) {
        // Домашний экран
        navigateWithSlideAnimation(route = BottomBarGraph.HOME) {
            HomeNavigationGraph(homeScreenViewModel = homeScreenViewModel, padding = padding)
        }

        // Поиск
        navigateWithSlideAnimation(route = BottomBarGraph.SEARCH) {
            SearchScreenScaffold(searchScreenViewModel = searchScreenViewModel, padding = padding)
        }

        // Избранное
        navigateWithSlideAnimation(route = BottomBarGraph.FAVOURITES) {
            FavouritesScreenScaffold(padding)
        }

        // Сравнение
        navigateWithSlideAnimation(route = BottomBarGraph.COMPARE) {
            CompareScreenScaffold(padding)
        }

        // Профиль пользователя
        navigateWithSlideAnimation(route = BottomBarGraph.USER_PROFILE) {
            UserProfileNavigationGraph(
                userProfileViewModel = userProfileViewModel,
                navigateToSplashScreen = navigateToSplashScreen,
                padding = padding,
            )
        }
    }
}
