package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.ravenzip.devicepicker.extensions.functions.composable
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.screens.user.AdminPanelScreen
import com.ravenzip.devicepicker.state.TopAppBarState
import com.ravenzip.devicepicker.viewmodels.TopAppBarViewModel

fun NavGraphBuilder.userProfileNavigationGraph(
    padding: PaddingValues,
    topAppBarViewModel: TopAppBarViewModel,
    bottomBarState: MutableState<Boolean>,
) {
    navigation(
        route = UserProfileGraph.USER_PROFILE_ROOT,
        startDestination = UserProfileGraph.ADMIN_PANEL) {
            composable(route = UserProfileGraph.ADMIN_PANEL) {
                topAppBarViewModel.setTopBarState(
                    TopAppBarState.createTopAppBarState("Панель администратора"))
                bottomBarState.value = false

                AdminPanelScreen(padding = padding)
            }
        }
}
