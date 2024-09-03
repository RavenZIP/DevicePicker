package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ravenzip.devicepicker.extensions.functions.navigateWithFadeAnimation
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.ui.screens.main.CompareScreen
import com.ravenzip.devicepicker.ui.screens.main.FavouritesScreen
import com.ravenzip.devicepicker.ui.screens.main.HomeScreen
import com.ravenzip.devicepicker.ui.screens.main.SearchScreen
import com.ravenzip.devicepicker.ui.screens.main.UserProfileScreen
import com.ravenzip.devicepicker.viewmodels.UserProfileViewModel

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    padding: PaddingValues,
    navigateToSplashScreen: () -> Unit,
) {
    val userProfileViewModel = hiltViewModel<UserProfileViewModel>()

    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME,
    ) {
        // Домашний экран
        navigateWithFadeAnimation(route = BottomBarGraph.HOME) {
            HomeScreen(
                padding = padding,
                navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) },
            )
        }

        homeNavigationGraph(padding = padding)

        // Поиск
        navigateWithFadeAnimation(route = BottomBarGraph.SEARCH) { SearchScreen(padding = padding) }

        // Избранное
        navigateWithFadeAnimation(route = BottomBarGraph.FAVOURITES) { FavouritesScreen(padding) }

        // Сравнение
        navigateWithFadeAnimation(route = BottomBarGraph.COMPARE) { CompareScreen(padding) }

        // Профиль пользователя
        navigateWithFadeAnimation(route = BottomBarGraph.USER_PROFILE) {
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
