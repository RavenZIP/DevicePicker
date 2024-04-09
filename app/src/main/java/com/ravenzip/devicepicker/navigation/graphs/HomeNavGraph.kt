package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.ravenzip.devicepicker.services.DeviceCompactService
import com.ravenzip.devicepicker.services.HomeScreenService
import com.ravenzip.devicepicker.services.ImageService
import com.ravenzip.devicepicker.services.TopAppBarService
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onCompletion

@Composable
fun HomeScreenNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    topAppBarService: TopAppBarService
) {
    val imagesService = hiltViewModel<ImageService>()
    val isLoadingDeviceCompact = remember { mutableStateOf(true) }
    val isLoadingImages = remember { mutableStateOf(false) }
    val deviceCompactService = hiltViewModel<DeviceCompactService>()
    val homeScreenService = hiltViewModel<HomeScreenService>()
    val devices = deviceCompactService.devices.collectAsState().value
    val images = deviceCompactService.images.collectAsState().value

    // Получаем компактную модель устройств
    // Грузим сразу все устройства, т.к. в дальнейшем компактная
    // модель будет использоваться для других экранов
    LaunchedEffect(isLoadingDeviceCompact.value) {
        if (isLoadingDeviceCompact.value) {
            deviceCompactService
                .get()
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
            images
                .map { imagesService.getImage(it) }
                .asFlow()
                .flatMapMerge(concurrency = 3) { it }
                .onCompletion { isLoadingImages.value = false }
                .collect {
                    homeScreenService.tryToSetImageFromPopularDevices(it)
                    homeScreenService.tryToSetImageFromLowPriceDevices(it)
                    homeScreenService.tryToSetImageFromHighPerformanceDevices(it)
                }
        }
    }

    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME
    ) {
        composable(route = BottomBarGraph.HOME) {
            topAppBarService.setTitle("Главная")
            HomeScreen(padding = padding, homeScreenService = homeScreenService)
        }

        composable(route = BottomBarGraph.SEARCH) {
            topAppBarService.setTitle("Поиск")
            SearchScreen(padding)
        }

        composable(route = BottomBarGraph.FAVOURITES) {
            topAppBarService.setTitle("Избранное")
            FavouritesScreen(padding)
        }

        composable(route = BottomBarGraph.COMPARE) {
            topAppBarService.setTitle("Сравнение")
            CompareScreen(padding)
        }

        composable(route = BottomBarGraph.USER_PROFILE) {
            topAppBarService.setTitle("Профиль")
            UserProfileScreen(padding)
        }
    }
}
