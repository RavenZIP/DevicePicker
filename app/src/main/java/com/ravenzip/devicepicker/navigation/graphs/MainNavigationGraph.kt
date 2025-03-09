package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ravenzip.devicepicker.common.utils.extension.navigateWithSlideAnimation
import com.ravenzip.devicepicker.features.main.compare.CompareScreenScaffold
import com.ravenzip.devicepicker.features.main.search.SearchScreenScaffold
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.features.main.home.HomeViewModel
import com.ravenzip.devicepicker.features.main.search.SearchViewModel
import com.ravenzip.devicepicker.features.main.user.UserProfileViewModel

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    padding: PaddingValues,
    navigateToSplashScreen: () -> Unit,
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val searchViewModel = hiltViewModel<SearchViewModel>()
    val userProfileViewModel = hiltViewModel<UserProfileViewModel>()

    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME,
    ) {
        // Домашний экран
        navigateWithSlideAnimation(route = BottomBarGraph.HOME) {
            HomeNavigationGraph(homeViewModel = homeViewModel, padding = padding)
        }

        // Поиск
        navigateWithSlideAnimation(route = BottomBarGraph.SEARCH) {
            SearchScreenScaffold(viewModel = searchViewModel, padding = padding)
        }

        // Избранное
        navigateWithSlideAnimation(route = BottomBarGraph.FAVOURITES) {
            FavouritesNavigationGraph(padding = padding)
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
