package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.ravenzip.devicepicker.extensions.functions.navigateWithSlideAnimation
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.ui.screens.user.AdminPanelScreen
import com.ravenzip.devicepicker.ui.screens.user.DeviceHistoryScreen

fun NavGraphBuilder.userProfileNavigationGraph(
    padding: PaddingValues,
    navigateToDevice: () -> Unit,
) {
    navigation(
        route = UserProfileGraph.USER_PROFILE_ROOT,
        startDestination = UserProfileGraph.ADMIN_PANEL,
    ) {
        navigateWithSlideAnimation(route = UserProfileGraph.ADMIN_PANEL) {
            AdminPanelScreen(padding = padding)
        }

        navigateWithSlideAnimation(route = UserProfileGraph.DEVICE_HISTORY) {
            DeviceHistoryScreen(padding = padding, navigateToDevice = navigateToDevice)
        }
    }
}
