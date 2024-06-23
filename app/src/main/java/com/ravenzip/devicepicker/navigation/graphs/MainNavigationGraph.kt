package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

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

    LaunchedEffect(Unit) {
        // Получаем компактную модель устройств и данные о пользователе
        // Грузим сразу все устройства, т.к. в дальнейшем компактная
        // модель будет использоваться для других экранов

        // Затем грузим урлы изображений
        launch {
            deviceViewModel
                .getDeviceCompactList()
                .zip(userViewModel.get(userViewModel.getUser())) { deviceCompactList, user ->
                    deviceViewModel.setDevicesFromCategories(deviceCompactList, user.searchHistory)
                    return@zip deviceCompactList
                }
                .flatMapConcat { deviceCompactList ->
                    imageViewModel.getImageUrls(deviceCompactList).flatMapMerge(concurrency = 3) {
                        it
                    }
                }
                .collect {
                    deviceViewModel.setImageUrlToDevices(it)
                    deviceViewModel.updateDevicesCategories()
                }
        }

        // Грузим данные о брендах и типах устройств
        launch {
            brandViewModel
                .getBrandList()
                .flatMapConcat { deviceTypeViewModel.getDeviceTypeList() }
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
