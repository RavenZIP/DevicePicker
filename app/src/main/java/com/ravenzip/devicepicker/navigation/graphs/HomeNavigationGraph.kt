package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.ravenzip.devicepicker.extensions.functions.navigateWithFadeAnimation
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.state.DeviceState
import com.ravenzip.devicepicker.ui.screens.home.DeviceInfoScreen
import kotlinx.coroutines.flow.StateFlow

fun NavGraphBuilder.homeNavigationGraph(
    padding: PaddingValues,
    deviceStateByViewModel: StateFlow<DeviceState>,
) {
    navigation(route = HomeGraph.HOME_ROOT, startDestination = HomeGraph.DEVICE_INFO) {
        navigateWithFadeAnimation(route = HomeGraph.DEVICE_INFO) {
            DeviceInfoScreen(padding = padding, deviceStateByViewModel = deviceStateByViewModel)
        }
    }
}
