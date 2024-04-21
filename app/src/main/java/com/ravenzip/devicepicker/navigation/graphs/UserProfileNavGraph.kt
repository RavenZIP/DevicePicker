package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.ravenzip.devicepicker.enums.TopAppBarEnum
import com.ravenzip.devicepicker.extensions.functions.composable
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.screens.main.UserProfileScreen
import com.ravenzip.devicepicker.screens.user.AdminPanelScreen
import com.ravenzip.devicepicker.services.TopAppBarService
import com.ravenzip.devicepicker.services.firebase.UserService

fun NavGraphBuilder.userProfileNavigationGraph(
    padding: PaddingValues,
    topAppBarService: TopAppBarService,
    bottomBarState: MutableState<Boolean>,
    userService: UserService,
    navController: NavController
) {
    navigation(
        route = BottomBarGraph.USER_PROFILE_ROOT,
        startDestination = UserProfileGraph.USER_PROFILE
    ) {
        composable(route = UserProfileGraph.USER_PROFILE) {
            topAppBarService.setText("Профиль")
            topAppBarService.setState(TopAppBarEnum.TopAppBar)
            bottomBarState.value = true

            UserProfileScreen(
                padding = padding,
                userService = userService,
                onClick = arrayOf({ navController.navigate(UserProfileGraph.ADMIN_PANEL) })
            )
        }

        composable(route = UserProfileGraph.ADMIN_PANEL) {
            topAppBarService.setText("Панель администратора")
            bottomBarState.value = false

            AdminPanelScreen(padding = padding)
        }
    }
}
