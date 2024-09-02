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
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel
import com.ravenzip.devicepicker.viewmodels.UserViewModel

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    padding: PaddingValues,
    navigateToSplashScreen: () -> Unit,
) {
    val userViewModel = hiltViewModel<UserViewModel>()
    val deviceViewModel = hiltViewModel<DeviceViewModel>()

    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME,
    ) {
        // Домашний экран
        navigateWithFadeAnimation(route = BottomBarGraph.HOME) {
            HomeScreen(
                padding = padding,
                categoriesStateByViewModel = deviceViewModel.categories,
                selectedCategoryByViewModel = deviceViewModel.selectedCategory,
                selectCategory = { item -> deviceViewModel.selectCategory(item) },
                navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) },
                getDevice = { uid, brand, model -> deviceViewModel.getDevice(uid, brand, model) },
            )
        }

        homeNavigationGraph(
            padding = padding,
            userDataByViewModel = userViewModel.user,
            updateDeviceHistory = { deviceHistory ->
                userViewModel.updateDeviceHistory(deviceHistory)
            },
            deviceStateByViewModel = deviceViewModel.deviceState,
        )

        // Поиск
        navigateWithFadeAnimation(route = BottomBarGraph.SEARCH) { SearchScreen(padding = padding) }

        // Избранное
        navigateWithFadeAnimation(route = BottomBarGraph.FAVOURITES) { FavouritesScreen(padding) }

        // Сравнение
        navigateWithFadeAnimation(route = BottomBarGraph.COMPARE) { CompareScreen(padding) }

        // Профиль пользователя
        navigateWithFadeAnimation(route = BottomBarGraph.USER_PROFILE) {
            UserProfileScreen(
                padding = padding,
                userDataByViewModel = userViewModel.user,
                onClickToAdminPanel = { navController.navigate(UserProfileGraph.ADMIN_PANEL) },
                onClickToDeviceHistory = {
                    navController.navigate(UserProfileGraph.DEVICE_HISTORY)
                },
                onClickToLogout = {
                    userViewModel.logout()
                    navigateToSplashScreen()
                },
            )
        }

        userProfileNavigationGraph(
            padding = padding,
            createDeviceHistoryList = { userDeviceHistoryUidList ->
                deviceViewModel.createDeviceHistoryList(userDeviceHistoryUidList)
            },
            userDataByViewModel = userViewModel.user,
            navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) },
            getDevice = { uid, brand, model -> deviceViewModel.getDevice(uid, brand, model) },
        )
    }
}
