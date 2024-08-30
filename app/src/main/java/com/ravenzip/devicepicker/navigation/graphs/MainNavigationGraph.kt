package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.extensions.functions.navigateWithFadeAnimation
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.ui.screens.main.CompareScreen
import com.ravenzip.devicepicker.ui.screens.main.FavouritesScreen
import com.ravenzip.devicepicker.ui.screens.main.HomeScreen
import com.ravenzip.devicepicker.ui.screens.main.SearchScreen
import com.ravenzip.devicepicker.ui.screens.main.UserProfileScreen
import com.ravenzip.devicepicker.viewmodels.BrandViewModel
import com.ravenzip.devicepicker.viewmodels.DeviceTypeViewModel
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    padding: PaddingValues,
    userDataByViewModel: StateFlow<User>,
    updateDeviceHistory: suspend (List<String>) -> Boolean,
    firebaseUser: FirebaseUser?,
    getUserData: suspend () -> Unit,
    onClickLogout: () -> Unit,
) {
    val deviceViewModel = hiltViewModel<DeviceViewModel>()
    val brandViewModel = hiltViewModel<BrandViewModel>()
    val deviceTypeViewModel = hiltViewModel<DeviceTypeViewModel>()

    LaunchedEffect(Unit) {
        // Получаем компактную модель устройств вместе с изображениями
        // Грузим сразу все устройства, т.к. в дальнейшем компактная
        // модель будет использоваться для других экранов
        launch { deviceViewModel.getDeviceCompactList() }

        // Данные пользователя
        launch { getUserData() }

        // Данные о брендах и типах устройств
        launch {
            brandViewModel
                .getBrandList()
                .zip(deviceTypeViewModel.getDeviceTypeList()) { _, _ -> }
                .collect {}
        }
    }

    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME,
    ) {
        // Домашний экран
        navigateWithFadeAnimation(route = BottomBarGraph.HOME) {
            HomeScreen(
                padding = padding,
                deviceCompactStateByViewModel = deviceViewModel.deviceCompactState,
                navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) },
                getDevice = { uid, brand, model -> deviceViewModel.getDevice(uid, brand, model) },
            )
        }

        homeNavigationGraph(
            padding = padding,
            userDataByViewModel = userDataByViewModel,
            updateDeviceHistory = updateDeviceHistory,
            deviceStateByViewModel = deviceViewModel.deviceState,
        )

        // Поиск
        navigateWithFadeAnimation(route = BottomBarGraph.SEARCH) {
            SearchScreen(
                padding = padding,
                listOfBrandByViewModel = brandViewModel.listOfBrand,
                listOfDeviceTypeByViewModel = deviceTypeViewModel.listOfDeviceType,
            )
        }

        // Избранное
        navigateWithFadeAnimation(route = BottomBarGraph.FAVOURITES) { FavouritesScreen(padding) }

        // Сравнение
        navigateWithFadeAnimation(route = BottomBarGraph.COMPARE) { CompareScreen(padding) }

        // Профиль пользователя
        navigateWithFadeAnimation(route = BottomBarGraph.USER_PROFILE) {
            UserProfileScreen(
                padding = padding,
                userDataByViewModel = userDataByViewModel,
                onClickLogout = onClickLogout,
                onClickAdminPanel = { navController.navigate(UserProfileGraph.ADMIN_PANEL) },
            )
        }

        userProfileNavigationGraph(padding = padding)
    }
}
