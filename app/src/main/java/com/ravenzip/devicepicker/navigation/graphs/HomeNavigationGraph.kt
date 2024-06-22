package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.enums.TopAppBarTypeEnum
import com.ravenzip.devicepicker.extensions.functions.composable
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.screens.home.DeviceInfoScreen
import com.ravenzip.devicepicker.state.TopAppBarState
import com.ravenzip.devicepicker.viewmodels.DeviceViewModel
import com.ravenzip.devicepicker.viewmodels.TopAppBarViewModel
import com.ravenzip.workshop.data.AppBarItem
import com.ravenzip.workshop.data.IconParameters

fun NavGraphBuilder.homeNavigationGraph(
    padding: PaddingValues,
    topAppBarViewModel: TopAppBarViewModel,
    deviceViewModel: DeviceViewModel,
    bottomBarState: MutableState<Boolean>,
    navController: NavHostController
) {
    navigation(route = HomeGraph.HOME_ROOT, startDestination = HomeGraph.DEVICE_INFO) {
        composable(route = HomeGraph.DEVICE_INFO) {
            val topAppBarState =
                TopAppBarState.createTopAppBarState(
                    backArrowOnClick = { navController.navigateUp() },
                    menuItems = topAppBarItemList())

            topAppBarViewModel.setTopBarState(topAppBarState)
            topAppBarViewModel.setType(TopAppBarTypeEnum.TopAppBar)
            bottomBarState.value = false

            DeviceInfoScreen(padding = padding, deviceViewModel = deviceViewModel)
        }
    }
}

@Composable
private fun topAppBarItemList(): List<AppBarItem> {
    val favouriteButton =
        AppBarItem(
            icon =
                IconParameters(value = ImageVector.vectorResource(R.drawable.i_heart), size = 20),
            onClick = {})

    val compareButton =
        AppBarItem(
            icon =
                IconParameters(value = ImageVector.vectorResource(R.drawable.i_compare), size = 20),
            onClick = {})

    return listOf(favouriteButton, compareButton)
}
