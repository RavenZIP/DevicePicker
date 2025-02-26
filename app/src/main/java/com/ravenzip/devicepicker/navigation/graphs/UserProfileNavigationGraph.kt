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
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.ui.screens.main.device.info.DeviceInfoScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.admin.AdminScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.company.CompanyScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.history.DeviceHistoryScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.profile.UserProfileScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.reviews.ReviewsScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.settings.UserSettingsScreenScaffold
import com.ravenzip.devicepicker.viewmodels.main.UserProfileViewModel

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
                navigateToSplashScreen = navigateToSplashScreen,
                padding = padding,
            )
        }

        navigateWithSlideAnimation(route = UserProfileGraph.ADMIN_PANEL) {
            AdminScreenScaffold(padding = padding)
        }

        navigateWithSlideAnimation(route = UserProfileGraph.USER_SETTINGS) {
            UserSettingsScreenScaffold(padding = padding)
        }

        navigateWithSlideAnimation(route = UserProfileGraph.COMPANY) {
            CompanyScreenScaffold(
                padding = padding,
                navigateBack = { navController.popBackStack() },
            )
        }

        navigateWithSlideAnimation(route = UserProfileGraph.DEVICE_HISTORY) {
            DeviceHistoryScreenScaffold(
                padding = padding,
                navigateToDevice = { uid ->
                    navController.navigate("${HomeGraph.DEVICE_INFO}/${uid}")
                },
            )
        }

        navigateWithSlideAnimation(route = UserProfileGraph.REVIEWS) {
            ReviewsScreenScaffold(padding = padding)
        }

        navigateWithFadeAnimation(route = "${HomeGraph.DEVICE_INFO}/{uid}") {
            DeviceInfoScreenScaffold(
                padding = padding,
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}
