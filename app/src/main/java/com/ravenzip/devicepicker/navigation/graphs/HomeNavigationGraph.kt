package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.TopAppBarTypeEnum
import com.ravenzip.devicepicker.extensions.functions.composable
import com.ravenzip.devicepicker.navigation.models.HomeGraph
import com.ravenzip.devicepicker.screens.home.DeviceInfoScreen
import com.ravenzip.devicepicker.state.DeviceState
import com.ravenzip.devicepicker.state.TopAppBarState
import com.ravenzip.workshop.data.AppBarItem
import com.ravenzip.workshop.data.IconParameters
import kotlinx.coroutines.flow.StateFlow

fun NavGraphBuilder.homeNavigationGraph(
    padding: PaddingValues,
    setTopAppBarState: (topAppBarState: TopAppBarState) -> Unit,
    setTopAppBarType: (topAppBarType: TopAppBarTypeEnum) -> Unit,
    bottomBarState: MutableState<Boolean>,
    deviceStateByViewModel: StateFlow<DeviceState>,
    navController: NavHostController
) {
    navigation(route = HomeGraph.HOME_ROOT, startDestination = HomeGraph.DEVICE_INFO) {
        composable(route = HomeGraph.DEVICE_INFO) {
            val topAppBarState =
                TopAppBarState.createTopAppBarState(
                    onClickToBackArrow = { navController.navigateUp() },
                    menuItems = topAppBarItemList())

            setTopAppBarState(topAppBarState)
            setTopAppBarType(TopAppBarTypeEnum.TopAppBar)
            bottomBarState.value = false

            DeviceInfoScreen(padding = padding, deviceStateByViewModel = deviceStateByViewModel)
        }
    }
}

@Composable
private fun topAppBarItemList(): List<AppBarItem> {
    val favouriteIcon =
        IconParameters(value = ImageVector.vectorResource(R.drawable.i_heart), size = 20)
    val compareIcon =
        IconParameters(value = ImageVector.vectorResource(R.drawable.i_compare), size = 20)

    val favouriteButton = remember {
        mutableStateOf(AppBarItem(icon = favouriteIcon, onClick = {}))
    }
    val compareButton = remember { mutableStateOf(AppBarItem(icon = compareIcon, onClick = {})) }

    return listOf(favouriteButton.value, compareButton.value)
}
