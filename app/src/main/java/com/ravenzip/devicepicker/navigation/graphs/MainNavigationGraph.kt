package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ravenzip.devicepicker.enums.TopAppBarStateEnum
import com.ravenzip.devicepicker.extensions.functions.composable
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.screens.main.CompareScreen
import com.ravenzip.devicepicker.screens.main.FavouritesScreen
import com.ravenzip.devicepicker.screens.main.HomeScreen
import com.ravenzip.devicepicker.screens.main.SearchScreen
import com.ravenzip.devicepicker.screens.main.UserProfileScreen
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel
import com.ravenzip.devicepicker.viewmodels.ImageViewModel
import com.ravenzip.devicepicker.viewmodels.TopAppBarViewModel
import com.ravenzip.devicepicker.viewmodels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onCompletion

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    padding: PaddingValues,
    topAppBarViewModel: TopAppBarViewModel,
    userViewModel: UserViewModel,
    bottomBarState: MutableState<Boolean>
) {
    val imageViewModel = hiltViewModel<ImageViewModel>()
    val deviceViewModel = hiltViewModel<DeviceViewModel>()

    val isLoadingDeviceCompact = remember { mutableStateOf(true) }
    val isLoadingImageUrls = remember { mutableStateOf(false) }
    val isLoadingUserData = remember { mutableStateOf(true) }

    val deviceCompactsState = deviceViewModel.deviceCompactState.collectAsState().value
    val deviceCompactList = deviceCompactsState.deviceCompactList

    // Получаем компактную модель устройств
    // Грузим сразу все устройства, т.к. в дальнейшем компактная
    // модель будет использоваться для других экранов
    LaunchedEffect(isLoadingDeviceCompact.value) {
        if (isLoadingDeviceCompact.value) {
            deviceViewModel
                .getDeviceCompactList()
                .onCompletion {
                    isLoadingDeviceCompact.value = false
                    isLoadingImageUrls.value = true
                }
                .collect { deviceViewModel.setDevicesFromCategories(deviceCompactList) }
        }
    }

    // Получаем урлы изображений
    LaunchedEffect(key1 = isLoadingImageUrls.value) {
        if (isLoadingImageUrls.value) {
            imageViewModel
                .getImageUrls(deviceCompactList)
                .flatMapMerge(concurrency = 3) { it }
                .onCompletion { isLoadingImageUrls.value = false }
                .collect {
                    deviceViewModel.setImageUrlToDevices(it)
                    delay(100)
                    deviceViewModel.updateDevicesCategories()
                }
        }
    }

    // Грузим данные о пользователе
    LaunchedEffect(key1 = isLoadingUserData.value) {
        if (isLoadingUserData.value) {
            userViewModel
                .get(userViewModel.getUser())
                .onCompletion { isLoadingUserData.value = false }
                .collect {}
        }
    }

    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME) {
            /// Домашний экран
            composable(route = BottomBarGraph.HOME) {
                topAppBarViewModel.setText("Главная")
                topAppBarViewModel.setState(TopAppBarStateEnum.TopAppBar)

                HomeScreen(
                    padding = padding,
                    deviceViewModel = deviceViewModel,
                    navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) })
            }

            homeNavigationGraph(
                padding = padding,
                topAppBarViewModel = topAppBarViewModel,
                deviceViewModel = deviceViewModel)

            /// Поиск
            composable(route = BottomBarGraph.SEARCH) {
                topAppBarViewModel.setText("Введите текст...")
                topAppBarViewModel.setState(TopAppBarStateEnum.SearchBar)

                SearchScreen(padding)
            }

            /// Избранное
            composable(route = BottomBarGraph.FAVOURITES) {
                topAppBarViewModel.setText("Избранное")
                topAppBarViewModel.setState(TopAppBarStateEnum.TopAppBar)

                FavouritesScreen(padding)
            }

            /// Сравнение
            composable(route = BottomBarGraph.COMPARE) {
                topAppBarViewModel.setText("Сравнение")
                topAppBarViewModel.setState(TopAppBarStateEnum.TopAppBar)

                CompareScreen(padding)
            }

            /// Профиль пользователя
            composable(route = BottomBarGraph.USER_PROFILE) {
                topAppBarViewModel.setText("Профиль")
                topAppBarViewModel.setState(TopAppBarStateEnum.TopAppBar)
                bottomBarState.value = true

                UserProfileScreen(
                    padding = padding,
                    userViewModel = userViewModel,
                    onClick = arrayOf({ navController.navigate(UserProfileGraph.ADMIN_PANEL) }))
            }

            userProfileNavigationGraph(
                padding,
                topAppBarViewModel = topAppBarViewModel,
                bottomBarState = bottomBarState,
            )
        }
}
