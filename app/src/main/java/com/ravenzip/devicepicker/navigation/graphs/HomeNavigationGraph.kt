package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.common.utils.extension.navigateWithFadeAnimation
import com.ravenzip.devicepicker.common.utils.extension.navigateWithSlideAnimation
import com.ravenzip.devicepicker.features.main.device.info.DeviceInfoScreenScaffold
import com.ravenzip.devicepicker.features.main.device.questions.QuestionsScreenScaffold
import com.ravenzip.devicepicker.features.main.device.reviews.ReviewsScreenScaffold
import com.ravenzip.devicepicker.features.main.home.HomeScreenScaffold
import com.ravenzip.devicepicker.features.main.home.HomeViewModel
import com.ravenzip.devicepicker.navigation.NavigationParams
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.DeviceInfoGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph

@Composable
fun HomeNavigationGraph(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel,
    padding: PaddingValues,
) {
    NavHost(
        navController = navController,
        route = BottomBarGraph.HOME,
        startDestination = HomeGraph.HOME_ROOT,
    ) {
        navigateWithSlideAnimation(route = HomeGraph.HOME_ROOT) {
            HomeScreenScaffold(
                viewModel = homeViewModel,
                padding = padding,
                navigateToDevice = { uid ->
                    navController.navigate("${HomeGraph.DEVICE_INFO}/${uid}")
                },
            )
        }

        navigateWithFadeAnimation(route = "${HomeGraph.DEVICE_INFO}/{uid}") {
            DeviceInfoScreenScaffold(
                padding = padding,
                navigationParams = NavigationParams.fromNavController(navController = navController),
            )
        }

        // TODO вынести в отдельный нав граф
        navigateWithFadeAnimation(route = "${DeviceInfoGraph.REVIEWS}/{uid}") {
            ReviewsScreenScaffold(
                navigateBack = { navController.popBackStack() },
                padding = padding,
            )
        }

        navigateWithFadeAnimation(route = "${DeviceInfoGraph.QUESTIONS}/{uid}") {
            QuestionsScreenScaffold(
                navigateBack = { navController.popBackStack() },
                padding = padding,
            )
        }
    }
}
