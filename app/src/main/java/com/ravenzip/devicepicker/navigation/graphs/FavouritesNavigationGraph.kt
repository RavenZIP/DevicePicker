package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.extensions.functions.navigateWithFadeAnimation
import com.ravenzip.devicepicker.extensions.functions.navigateWithSlideAnimation
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.FavouritesGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.ui.screens.main.device.info.DeviceInfoScaffold
import com.ravenzip.devicepicker.ui.screens.main.favourites.FavouritesScreenScaffold

@Composable
fun FavouritesNavigationGraph(
    navController: NavHostController = rememberNavController(),
    padding: PaddingValues,
) {
    NavHost(
        navController = navController,
        route = BottomBarGraph.USER_PROFILE,
        startDestination = FavouritesGraph.FAVOURITES_ROOT,
    ) {
        navigateWithSlideAnimation(FavouritesGraph.FAVOURITES_ROOT) {
            FavouritesScreenScaffold(
                padding = padding,
                navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) },
            )
        }

        navigateWithFadeAnimation(route = HomeGraph.DEVICE_INFO) {
            DeviceInfoScaffold(padding = padding, navigateBack = { navController.popBackStack() })
        }
    }
}
