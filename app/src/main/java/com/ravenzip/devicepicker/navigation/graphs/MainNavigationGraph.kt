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
import com.ravenzip.devicepicker.services.HomeScreenService
import com.ravenzip.devicepicker.viewmodels.DeviceCompactViewModel
import com.ravenzip.devicepicker.viewmodels.ImageViewModel
import com.ravenzip.devicepicker.viewmodels.TopAppBarViewModel
import com.ravenzip.devicepicker.viewmodels.UserViewModel
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
    val deviceCompactViewModel = hiltViewModel<DeviceCompactViewModel>()
    val homeScreenService = hiltViewModel<HomeScreenService>()

    val isLoadingDeviceCompact = remember { mutableStateOf(true) }
    val isLoadingImages = remember { mutableStateOf(false) }
    val isLoadingUserData = remember { mutableStateOf(true) }

    val devices = deviceCompactViewModel.devices.collectAsState().value
    val images = deviceCompactViewModel.images.collectAsState().value

    // Получаем компактную модель устройств
    // Грузим сразу все устройства, т.к. в дальнейшем компактная
    // модель будет использоваться для других экранов
    LaunchedEffect(isLoadingDeviceCompact.value) {
        if (isLoadingDeviceCompact.value) {
            deviceCompactViewModel
                .getDevices()
                .onCompletion {
                    isLoadingDeviceCompact.value = false
                    isLoadingImages.value = true
                }
                .collect { homeScreenService.setDevicesFromCategories(devices) }
        }
    }

    // Грузим изображения
    LaunchedEffect(key1 = isLoadingImages.value) {
        if (isLoadingImages.value) {
            imageViewModel
                .getImages(images)
                .flatMapMerge(concurrency = 3) { it }
                .onCompletion { isLoadingImages.value = false }
                .collect {
                    homeScreenService.tryToSetImageFromPopularDevices(it)
                    homeScreenService.tryToSetImageFromLowPriceDevices(it)
                    homeScreenService.tryToSetImageFromHighPerformanceDevices(it)
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
        startDestination = BottomBarGraph.HOME
    ) {
        composable(route = BottomBarGraph.HOME) {
            topAppBarViewModel.setText("Главная")
            topAppBarViewModel.setState(TopAppBarStateEnum.TopAppBar)

            HomeScreen(
                padding = padding,
                homeScreenService = homeScreenService,
                navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) }
            )
        }

        homeNavigationGraph(
            padding = padding,
            topAppBarViewModel = topAppBarViewModel,
        )

        composable(route = BottomBarGraph.SEARCH) {
            topAppBarViewModel.setText("Введите текст...")
            topAppBarViewModel.setState(TopAppBarStateEnum.SearchBar)

            SearchScreen(padding)
        }

        composable(route = BottomBarGraph.FAVOURITES) {
            topAppBarViewModel.setText("Избранное")
            topAppBarViewModel.setState(TopAppBarStateEnum.TopAppBar)

            FavouritesScreen(padding)
        }

        composable(route = BottomBarGraph.COMPARE) {
            topAppBarViewModel.setText("Сравнение")
            topAppBarViewModel.setState(TopAppBarStateEnum.TopAppBar)

            CompareScreen(padding)
        }

        composable(route = BottomBarGraph.USER_PROFILE) {
            topAppBarViewModel.setText("Профиль")
            topAppBarViewModel.setState(TopAppBarStateEnum.TopAppBar)
            bottomBarState.value = true

            UserProfileScreen(
                padding = padding,
                userViewModel = userViewModel,
                onClick = arrayOf({ navController.navigate(UserProfileGraph.ADMIN_PANEL) })
            )
        }

        userProfileNavigationGraph(
            padding,
            topAppBarViewModel = topAppBarViewModel,
            bottomBarState = bottomBarState,
        )
    }
}
