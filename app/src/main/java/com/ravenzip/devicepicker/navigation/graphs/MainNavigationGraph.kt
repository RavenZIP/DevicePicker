package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ravenzip.devicepicker.extensions.functions.navigateWithSlideAnimation
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.ui.screens.main.CompareScreen
import com.ravenzip.devicepicker.ui.screens.main.FavouritesScreen
import com.ravenzip.devicepicker.ui.screens.main.HomeScreen
import com.ravenzip.devicepicker.ui.screens.main.SearchScreen
import com.ravenzip.devicepicker.ui.screens.main.UserProfileScreen
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
            HomeScreen(
                homeScreenViewModel = homeScreenViewModel,
                padding = padding,
                navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) },
            )
        }

        homeNavigationGraph(padding = padding)

        // Поиск
        navigateWithSlideAnimation(route = BottomBarGraph.SEARCH) {
            SearchScreen(searchScreenViewModel = searchScreenViewModel, padding = padding)
        }

        // Избранное
        navigateWithSlideAnimation(route = BottomBarGraph.FAVOURITES) { FavouritesScreen(padding) }

        // Сравнение
        navigateWithSlideAnimation(route = BottomBarGraph.COMPARE) { CompareScreen(padding) }

        // Профиль пользователя
        navigateWithSlideAnimation(route = BottomBarGraph.USER_PROFILE) {
            UserProfileScreen(
                userProfileViewModel = userProfileViewModel,
                onClickToAdminPanel = { navController.navigate(UserProfileGraph.ADMIN_PANEL) },
                onClickToDeviceHistory = {
                    navController.navigate(UserProfileGraph.DEVICE_HISTORY)
                },
                navigateToSplashScreen = navigateToSplashScreen,
                padding = padding,
            )
        }

        userProfileNavigationGraph(
            padding = padding,
            navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) },
        )
    }
}
