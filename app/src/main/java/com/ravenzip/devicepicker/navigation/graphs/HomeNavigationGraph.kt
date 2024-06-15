package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.ravenzip.devicepicker.enums.TopAppBarStateEnum
import com.ravenzip.devicepicker.extensions.functions.composable
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.screens.main.DeviceInfoScreen
import com.ravenzip.devicepicker.services.TopAppBarService

fun NavGraphBuilder.homeNavigationGraph(
    padding: PaddingValues,
    topAppBarService: TopAppBarService,
) {
    navigation(route = HomeGraph.HOME_ROOT, startDestination = HomeGraph.DEVICE_INFO) {
        composable(route = HomeGraph.DEVICE_INFO) {
            topAppBarService.setText("Информация об устройстве")
            topAppBarService.setState(TopAppBarStateEnum.TopAppBar)

            DeviceInfoScreen(padding = padding)
        }
    }
}