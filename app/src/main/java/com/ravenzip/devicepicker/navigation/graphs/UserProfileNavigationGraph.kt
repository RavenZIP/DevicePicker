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
import com.ravenzip.devicepicker.ui.screens.main.device.info.DeviceInfoScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.admin.AdminScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.history.DeviceInfoScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.profile.UserProfileScaffold
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
                userProfileViewModel = userProfileViewModel,
                onClickToAdminPanel = { navController.navigate(UserProfileGraph.ADMIN_PANEL) },
                onClickToDeviceHistory = {
                    navController.navigate(UserProfileGraph.DEVICE_HISTORY)
                },
                navigateToSplashScreen = navigateToSplashScreen,
                padding = padding,
            )
        }

        navigateWithSlideAnimation(route = UserProfileGraph.ADMIN_PANEL) {
            AdminScreenScaffold(padding = padding)
        }

        navigateWithSlideAnimation(route = UserProfileGraph.DEVICE_HISTORY) { uid ->
            DeviceInfoScreenScaffold(
                padding = padding,
                navigateToDevice = { navController.navigate("${HomeGraph.DEVICE_INFO}/${uid}") },
            )
        }

        navigateWithFadeAnimation(route = "${HomeGraph.DEVICE_INFO}/{uid}") {
            DeviceInfoScaffold(padding = padding, navigateBack = { navController.popBackStack() })
        }
    }
}
