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
import com.ravenzip.devicepicker.enums.TopAppBarTypeEnum
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
import com.ravenzip.devicepicker.state.SearchBarState
import com.ravenzip.devicepicker.state.TopAppBarState
import com.ravenzip.devicepicker.viewmodels.BrandViewModel
import com.ravenzip.devicepicker.viewmodels.DeviceTypeViewModel
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel
import com.ravenzip.devicepicker.viewmodels.ImageViewModel
import com.ravenzip.devicepicker.viewmodels.TopAppBarViewModel
import com.ravenzip.devicepicker.viewmodels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapConcat
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
    val brandViewModel = hiltViewModel<BrandViewModel>()
    val deviceTypeViewModel = hiltViewModel<DeviceTypeViewModel>()

    val isLoadingDeviceCompact = remember { mutableStateOf(true) }
    val isLoadingImageUrls = remember { mutableStateOf(false) }
    val isLoadingUserData = remember { mutableStateOf(false) }
    val isLoadingBrandAndDeviceType = remember { mutableStateOf(false) }

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
                .onCompletion {
                    isLoadingImageUrls.value = false
                    isLoadingUserData.value = true
                }
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
                .onCompletion {
                    isLoadingUserData.value = false
                    isLoadingBrandAndDeviceType.value = true
                }
                .collect {}
        }
    }

    // Грузим данные о брендах и типах устройств
    LaunchedEffect(key1 = isLoadingBrandAndDeviceType.value) {
        if (isLoadingBrandAndDeviceType.value) {
            brandViewModel
                .getBrandList()
                .flatMapConcat { deviceTypeViewModel.getDeviceTypeList() }
                .onCompletion { isLoadingBrandAndDeviceType.value = false }
                .collect {}
        }
    }

    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME) {
            /// Домашний экран
            composable(route = BottomBarGraph.HOME) {
                topAppBarViewModel.setTopBarState(TopAppBarState.createTopAppBarState("Главная"))
                topAppBarViewModel.setType(TopAppBarTypeEnum.TopAppBar)
                bottomBarState.value = true

                HomeScreen(
                    padding = padding,
                    deviceViewModel = deviceViewModel,
                    imageViewModel = imageViewModel,
                    navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) })
            }

            homeNavigationGraph(
                padding = padding,
                topAppBarViewModel = topAppBarViewModel,
                deviceViewModel = deviceViewModel,
                bottomBarState = bottomBarState,
                navController = navController)

            /// Поиск
            composable(route = BottomBarGraph.SEARCH) {
                topAppBarViewModel.setSearchBarState(SearchBarState())
                topAppBarViewModel.setType(TopAppBarTypeEnum.SearchBar)

                SearchScreen(
                    padding,
                    brandViewModel = brandViewModel,
                    deviceTypeViewModel = deviceTypeViewModel)
            }

            /// Избранное
            composable(route = BottomBarGraph.FAVOURITES) {
                topAppBarViewModel.setTopBarState(TopAppBarState.createTopAppBarState("Избранное"))
                topAppBarViewModel.setType(TopAppBarTypeEnum.TopAppBar)

                FavouritesScreen(padding)
            }

            /// Сравнение
            composable(route = BottomBarGraph.COMPARE) {
                topAppBarViewModel.setTopBarState(TopAppBarState.createTopAppBarState("Сравнение"))
                topAppBarViewModel.setType(TopAppBarTypeEnum.TopAppBar)

                CompareScreen(padding)
            }

            /// Профиль пользователя
            composable(route = BottomBarGraph.USER_PROFILE) {
                topAppBarViewModel.setTopBarState(TopAppBarState.createTopAppBarState("Профиль"))
                topAppBarViewModel.setType(TopAppBarTypeEnum.TopAppBar)
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
