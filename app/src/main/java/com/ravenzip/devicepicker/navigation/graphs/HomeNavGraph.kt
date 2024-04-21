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
import com.ravenzip.devicepicker.enums.TopAppBarEnum
import com.ravenzip.devicepicker.extensions.functions.composable
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.screens.main.CompareScreen
import com.ravenzip.devicepicker.screens.main.FavouritesScreen
import com.ravenzip.devicepicker.screens.main.HomeScreen
import com.ravenzip.devicepicker.screens.main.SearchScreen
import com.ravenzip.devicepicker.services.HomeScreenService
import com.ravenzip.devicepicker.services.TopAppBarService
import com.ravenzip.devicepicker.services.firebase.DeviceCompactService
import com.ravenzip.devicepicker.services.firebase.ImageService
import com.ravenzip.devicepicker.services.firebase.UserService
import com.ravenzip.devicepicker.services.firebase.getUser
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onCompletion

@Composable
fun HomeScreenNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    topAppBarService: TopAppBarService,
    userService: UserService,
    bottomBarState: MutableState<Boolean>
) {
    val imagesService = hiltViewModel<ImageService>()
    val deviceCompactService = hiltViewModel<DeviceCompactService>()
    val homeScreenService = hiltViewModel<HomeScreenService>()

    val isLoadingDeviceCompact = remember { mutableStateOf(true) }
    val isLoadingImages = remember { mutableStateOf(false) }
    val isLoadingUserData = remember { mutableStateOf(true) }

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

    // Грузим данные о пользователе
    LaunchedEffect(key1 = isLoadingUserData.value) {
        if (isLoadingUserData.value) {
            userService.get(getUser()).onCompletion { isLoadingUserData.value = false }.collect {}
        }
    }

    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME
    ) {
        composable(route = BottomBarGraph.HOME) {
            topAppBarService.setText("Главная")
            topAppBarService.setState(TopAppBarEnum.TopAppBar)
            HomeScreen(padding = padding, homeScreenService = homeScreenService)
        }

        composable(route = BottomBarGraph.SEARCH) {
            topAppBarService.setText("Введите текст...")
            topAppBarService.setState(TopAppBarEnum.SearchBar)
            SearchScreen(padding)
        }

        composable(route = BottomBarGraph.FAVOURITES) {
            topAppBarService.setText("Избранное")
            topAppBarService.setState(TopAppBarEnum.TopAppBar)
            FavouritesScreen(padding)
        }

        composable(route = BottomBarGraph.COMPARE) {
            topAppBarService.setText("Сравнение")
            topAppBarService.setState(TopAppBarEnum.TopAppBar)
            CompareScreen(padding)
        }

        userProfileNavigationGraph(
            padding,
            topAppBarService = topAppBarService,
            bottomBarState = bottomBarState,
            userService = userService,
            navController = navController
        )
    }
}
