package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.constants.enums.TopAppBarTypeEnum
import com.ravenzip.devicepicker.extensions.functions.composable
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.state.SearchBarState
import com.ravenzip.devicepicker.state.TopAppBarState
import com.ravenzip.devicepicker.ui.screens.main.CompareScreen
import com.ravenzip.devicepicker.ui.screens.main.FavouritesScreen
import com.ravenzip.devicepicker.ui.screens.main.HomeScreen
import com.ravenzip.devicepicker.ui.screens.main.SearchScreen
import com.ravenzip.devicepicker.ui.screens.main.UserProfileScreen
import com.ravenzip.devicepicker.viewmodels.BrandViewModel
import com.ravenzip.devicepicker.viewmodels.DeviceTypeViewModel
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel
import com.ravenzip.devicepicker.viewmodels.ImageViewModel
import com.ravenzip.devicepicker.viewmodels.TopAppBarViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    padding: PaddingValues,
    topAppBarViewModel: TopAppBarViewModel,
    userDataByViewModel: StateFlow<User>,
    getUser: () -> FirebaseUser?,
    getUserData: suspend (user: FirebaseUser?) -> Flow<User>,
    logout: suspend () -> Unit,
    bottomBarState: MutableState<Boolean>,
) {
    val imageViewModel = hiltViewModel<ImageViewModel>()
    val deviceViewModel = hiltViewModel<DeviceViewModel>()
    val brandViewModel = hiltViewModel<BrandViewModel>()
    val deviceTypeViewModel = hiltViewModel<DeviceTypeViewModel>()

    val deviceCompactState = deviceViewModel.deviceCompactState.collectAsState().value
    val deviceCompactList = deviceCompactState.deviceCompactList

    LaunchedEffect(Unit) {
        // Получаем компактную модель устройств и данные о пользователе
        // Грузим сразу все устройства, т.к. в дальнейшем компактная
        // модель будет использоваться для других экранов
        launch {
            deviceViewModel
                .getDeviceCompactList()
                .zip(getUserData(getUser())) { _, user ->
                    deviceViewModel.setUserSearchHistoryUidList(user.searchHistory)
                    deviceViewModel.createDeviceCompactStateList()
                }
                .collect {}

            // Грузим урлы изображений
            imageViewModel
                .getImageUrls(deviceCompactList)
                .flatMapMerge(concurrency = 3) { it }
                .collect { deviceViewModel.setImageUrlToDevices(it) }
        }

        // Грузим данные о брендах и типах устройств
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
        // / Домашний экран
        composable(route = BottomBarGraph.HOME) {
            topAppBarViewModel.setTopAppBarState(TopAppBarState.createTopAppBarState("Главная"))
            topAppBarViewModel.setType(TopAppBarTypeEnum.TopAppBar)
            bottomBarState.value = true

            HomeScreen(
                padding = padding,
                deviceCompactStateByViewModel = deviceViewModel.deviceCompactState,
                onClickToDeviceCard = { device, changeIsLoading ->
                    onClickToDeviceCard(
                        device = device,
                        deviceViewModel = deviceViewModel,
                        imageViewModel = imageViewModel,
                        changeIsLoading = changeIsLoading,
                        navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) },
                    )
                },
            )
        }

        homeNavigationGraph(
            padding = padding,
            setTopAppBarState = { topAppBarState ->
                topAppBarViewModel.setTopAppBarState(topAppBarState)
            },
            setTopAppBarType = { topAppBarType -> topAppBarViewModel.setType(topAppBarType) },
            bottomBarState = bottomBarState,
            deviceStateByViewModel = deviceViewModel.deviceState,
            navController = navController,
        )

        // / Поиск
        composable(route = BottomBarGraph.SEARCH) {
            topAppBarViewModel.setSearchBarState(SearchBarState.createSearchBarState())
            topAppBarViewModel.setType(TopAppBarTypeEnum.SearchBar)

            SearchScreen(
                padding = padding,
                listOfBrandByViewModel = brandViewModel.listOfBrand,
                listOfDeviceTypeByViewModel = deviceTypeViewModel.listOfDeviceType,
            )
        }

        // / Избранное
        composable(route = BottomBarGraph.FAVOURITES) {
            topAppBarViewModel.setTopAppBarState(
                TopAppBarState.createTopAppBarState("Избранное"),
            )
            topAppBarViewModel.setType(TopAppBarTypeEnum.TopAppBar)

            FavouritesScreen(padding)
        }

        // / Сравнение
        composable(route = BottomBarGraph.COMPARE) {
            topAppBarViewModel.setTopAppBarState(
                TopAppBarState.createTopAppBarState("Сравнение"),
            )
            topAppBarViewModel.setType(TopAppBarTypeEnum.TopAppBar)

            CompareScreen(padding)
        }

        // / Профиль пользователя
        composable(route = BottomBarGraph.USER_PROFILE) {
            topAppBarViewModel.setTopAppBarState(TopAppBarState.createTopAppBarState("Профиль"))
            topAppBarViewModel.setType(TopAppBarTypeEnum.TopAppBar)
            bottomBarState.value = true

            UserProfileScreen(
                padding = padding,
                userDataByViewModel = userDataByViewModel,
                logout = logout,
                onClick = arrayOf({ navController.navigate(UserProfileGraph.ADMIN_PANEL) }),
            )
        }

        userProfileNavigationGraph(
            padding = padding,
            setTopAppBarState = { topAppBarState ->
                topAppBarViewModel.setTopAppBarState(topAppBarState)
            },
            bottomBarState = bottomBarState,
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun onClickToDeviceCard(
    device: DeviceCompact,
    deviceViewModel: DeviceViewModel,
    imageViewModel: ImageViewModel,
    changeIsLoading: (Boolean) -> Unit,
    navigateToDevice: () -> Unit,
) {
    changeIsLoading(true)
    val cachedDevice = deviceViewModel.getCachedDevice(device.uid)

    if (cachedDevice == null) {
        deviceViewModel
            .getDeviceByBrandAndUid(brand = device.brand, uid = device.uid)
            .flatMapLatest {
                imageViewModel.getImageUrls(brand = device.brand, model = device.model)
            }
            .collect { deviceViewModel.setImageUrlToDevices(it) }
    }

    changeIsLoading(false)
    navigateToDevice()
}
