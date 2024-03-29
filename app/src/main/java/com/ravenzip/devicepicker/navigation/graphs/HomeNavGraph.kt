package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
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
import com.ravenzip.devicepicker.services.ImageService
import com.ravenzip.devicepicker.services.LowPriceDevicesService
import com.ravenzip.devicepicker.services.PopularDevicesService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun HomeScreenNavGraph(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = navController,
        route = RootGraph.MAIN,
        startDestination = BottomBarGraph.HOME
    ) {
        composable(route = BottomBarGraph.HOME) {
            val popularDevicesService = hiltViewModel<PopularDevicesService>()
            val lowPriceDevicesService = hiltViewModel<LowPriceDevicesService>()
            val imagesService = hiltViewModel<ImageService>()
            val isLoading = remember { mutableStateListOf(true, false, false, false, false) }
            val images = popularDevicesService.images.collectAsState().value
            val images2 = lowPriceDevicesService.images.collectAsState().value

            // Получаем текстовую инфорацию по всем категориям
            LaunchedEffect(key1 = isLoading[0]) {
                if (isLoading[0]) {
                    flowOf(popularDevicesService.get(), lowPriceDevicesService.get())
                        .flatMapMerge { it }
                        .onCompletion {
                            isLoading[0] = false
                            isLoading[1] = true
                            isLoading[2] = true
                        }
                        .collect {}
                }
            }

            // Грузим изображения из первой категории и обновляем модель устройств
            LaunchedEffect(key1 = isLoading[1]) {
                if (isLoading[1]) {
                    images
                        .map { imagesService.getImage(it) }
                        .asFlow()
                        .flatMapMerge { it }
                        .onCompletion { isLoading[1] = false }
                        .collect { popularDevicesService.setImage(it) }
                }
            }

            // Грузим изображения из второй категории и обновляем модель устройств
            LaunchedEffect(key1 = isLoading[2]) {
                if (isLoading[2]) {
                    images2
                        .map { imagesService.getImage(it) }
                        .asFlow()
                        .flatMapMerge { it }
                        .onCompletion { isLoading[2] = false }
                        .collect { lowPriceDevicesService.setImage(it) }
                }
            }

            HomeScreen(
                padding = padding,
                popularDevicesService = popularDevicesService,
                lowPriceDevicesService = lowPriceDevicesService
            )
        }
        composable(route = BottomBarGraph.SEARCH) { SearchScreen(padding) }
        composable(route = BottomBarGraph.FAVOURITES) { FavouritesScreen(padding) }
        composable(route = BottomBarGraph.COMPARE) { CompareScreen(padding) }
        composable(route = BottomBarGraph.USER_PROFILE) { UserProfileScreen(padding) }
    }
}
