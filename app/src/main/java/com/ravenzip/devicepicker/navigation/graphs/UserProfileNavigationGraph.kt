package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.common.utils.extension.navigateWithFadeAnimation
import com.ravenzip.devicepicker.common.utils.extension.navigateWithSlideAnimation
import com.ravenzip.devicepicker.features.main.device.info.DeviceInfoScreenScaffold
import com.ravenzip.devicepicker.features.main.user.UserProfileViewModel
import com.ravenzip.devicepicker.features.main.user.admin.AdminScreenScaffold
import com.ravenzip.devicepicker.features.main.user.devices.UserDevicesScreenScaffold
import com.ravenzip.devicepicker.features.main.user.history.DeviceHistoryScreenScaffold
import com.ravenzip.devicepicker.features.main.user.profile.UserProfileScaffold
import com.ravenzip.devicepicker.features.main.user.reviews.ReviewsScreenScaffold
import com.ravenzip.devicepicker.features.main.user.settings.user.UserSettingsScreenScaffold
import com.ravenzip.devicepicker.features.main.user.settings.visual.VisualAppearanceScreenScaffold
import com.ravenzip.devicepicker.features.main.user.updates.UpdatesScreenScaffold
import com.ravenzip.devicepicker.navigation.NavigationParams
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph

@Composable
fun UserProfileNavigationGraph(
    navController: NavHostController = rememberNavController(),
    userProfileViewModel: UserProfileViewModel,
    padding: PaddingValues,
    navigateToSplashScreen: () -> Unit,
) {
    NavHost(
        navController = navController,
        route = BottomBarGraph.USER_PROFILE,
        startDestination = UserProfileGraph.USER_PROFILE_ROOT,
    ) {
        navigateWithSlideAnimation(UserProfileGraph.USER_PROFILE_ROOT) {
            UserProfileScaffold(
                viewModel = userProfileViewModel,
                navigateToAdminPanel = { navController.navigate(UserProfileGraph.ADMIN_PANEL) },
                navigateToUserSettings = { navController.navigate(UserProfileGraph.USER_SETTINGS) },
                navigateToCompany = { navController.navigate(UserProfileGraph.COMPANY) },
                navigateToDeviceHistory = {
                    navController.navigate(UserProfileGraph.DEVICE_HISTORY)
                },
                navigateToReviews = { navController.navigate(UserProfileGraph.REVIEWS) },
                navigateToUserDevices = { navController.navigate(UserProfileGraph.USER_DEVICES) },
                navigateToVisualAppearance = {
                    navController.navigate(UserProfileGraph.VISUAL_APPEARANCE)
                },
                navigateToUpdates = { navController.navigate(UserProfileGraph.UPDATES) },
                navigateToSplashScreen = navigateToSplashScreen,
                padding = padding,
            )
        }

        navigateWithSlideAnimation(route = UserProfileGraph.ADMIN_PANEL) {
            AdminScreenScaffold(
                padding = padding,
                navigateToUserProfile = { navController.popBackStack() },
            )
        }

        navigateWithSlideAnimation(route = UserProfileGraph.USER_SETTINGS) {
            UserSettingsScreenScaffold(
                padding = padding,
                navigateToUserProfile = { navController.popBackStack() },
            )
        }

        // Домашний экран
        navigateWithSlideAnimation(route = UserProfileGraph.COMPANY) {
            CompanyNavigationGraph(
                padding = padding,
                navigateToUserProfile = { navController.popBackStack() },
            )
        }

        navigateWithSlideAnimation(route = UserProfileGraph.DEVICE_HISTORY) {
            DeviceHistoryScreenScaffold(
                padding = padding,
                navigateToUserProfile = { navController.popBackStack() },
                navigateToDevice = { uid ->
                    navController.navigate("${HomeGraph.DEVICE_INFO}/${uid}")
                },
            )
        }

        navigateWithSlideAnimation(route = UserProfileGraph.REVIEWS) {
            ReviewsScreenScaffold(padding = padding)
        }

        navigateWithSlideAnimation(route = UserProfileGraph.USER_DEVICES) {
            UserDevicesScreenScaffold(padding = padding)
        }

        navigateWithSlideAnimation(route = UserProfileGraph.VISUAL_APPEARANCE) {
            VisualAppearanceScreenScaffold(padding = padding)
        }

        navigateWithSlideAnimation(route = UserProfileGraph.UPDATES) {
            UpdatesScreenScaffold(padding = padding)
        }

        navigateWithFadeAnimation(route = "${HomeGraph.DEVICE_INFO}/{uid}") {
            DeviceInfoScreenScaffold(
                padding = padding,
                navigationParams = NavigationParams.fromNavController(navController),
            )
        }
    }
}
