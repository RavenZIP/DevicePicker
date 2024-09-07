package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.extensions.functions.navigateWithFadeAnimation
import com.ravenzip.devicepicker.extensions.functions.navigateWithSlideAnimation
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.ui.screens.main.device.info.DeviceInfoScaffold
import com.ravenzip.devicepicker.ui.screens.main.home.HomeScreenScaffold
import com.ravenzip.devicepicker.viewmodels.main.HomeScreenViewModel

@Composable
fun HomeNavigationGraph(
    navController: NavHostController = rememberNavController(),
    homeScreenViewModel: HomeScreenViewModel,
    padding: PaddingValues,
) {
    NavHost(
        navController = navController,
        route = BottomBarGraph.HOME,
        startDestination = HomeGraph.HOME_ROOT,
    ) {
        navigateWithSlideAnimation(route = HomeGraph.HOME_ROOT) {
            HomeScreenScaffold(
                homeScreenViewModel = homeScreenViewModel,
                padding = padding,
                navigateToDevice = { navController.navigate(HomeGraph.DEVICE_INFO) },
            )
        }

        navigateWithFadeAnimation(route = HomeGraph.DEVICE_INFO) {
            DeviceInfoScaffold(padding = padding, navigateBack = { navController.popBackStack() })
        }
    }
}
