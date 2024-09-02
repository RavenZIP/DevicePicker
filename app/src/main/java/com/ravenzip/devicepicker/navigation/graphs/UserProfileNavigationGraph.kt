package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.ravenzip.devicepicker.extensions.functions.navigateWithFadeAnimation
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.ui.screens.user.AdminPanelScreen
import com.ravenzip.devicepicker.ui.screens.user.DeviceHistoryScreen
import kotlinx.coroutines.flow.StateFlow

fun NavGraphBuilder.userProfileNavigationGraph(
    padding: PaddingValues,
    createDeviceHistoryList: (List<String>) -> List<DeviceCompact>,
    userDataByViewModel: StateFlow<User>,
    getDevice: (uid: String, brand: String, model: String) -> Unit,
    navigateToDevice: () -> Unit,
) {
    navigation(
        route = UserProfileGraph.USER_PROFILE_ROOT,
        startDestination = UserProfileGraph.ADMIN_PANEL,
    ) {
        navigateWithFadeAnimation(route = UserProfileGraph.ADMIN_PANEL) {
            AdminPanelScreen(padding = padding)
        }

        navigateWithFadeAnimation(route = UserProfileGraph.DEVICE_HISTORY) {
            DeviceHistoryScreen(
                padding = padding,
                createDeviceHistoryList = createDeviceHistoryList,
                userDataByViewModel = userDataByViewModel,
                getDevice = getDevice,
                navigateToDevice = navigateToDevice,
            )
        }
    }
}
