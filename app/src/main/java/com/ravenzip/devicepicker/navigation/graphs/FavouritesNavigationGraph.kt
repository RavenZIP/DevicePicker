package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.common.utils.extension.navigateWithFadeAnimation
import com.ravenzip.devicepicker.common.utils.extension.navigateWithSlideAnimation
import com.ravenzip.devicepicker.features.main.device.info.DeviceInfoScreenScaffold
import com.ravenzip.devicepicker.features.main.favourites.FavouritesScreenScaffold
import com.ravenzip.devicepicker.navigation.NavigationParams
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.FavouritesGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph

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
                navigateToDevice = { uid ->
                    navController.navigate("${HomeGraph.DEVICE_INFO}/${uid}")
                },
            )
        }

        navigateWithFadeAnimation(route = "${HomeGraph.DEVICE_INFO}/{uid}") {
            DeviceInfoScreenScaffold(
                padding = padding,
                navigationParams = NavigationParams.fromNavController(navController),
            )
        }
    }
}
