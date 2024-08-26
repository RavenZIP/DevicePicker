package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.ravenzip.devicepicker.extensions.functions.navigateWithFadeAnimation
import com.ravenzip.devicepicker.navigation.models.UserProfileGraph
import com.ravenzip.devicepicker.ui.screens.user.AdminPanelScreen

fun NavGraphBuilder.userProfileNavigationGraph(padding: PaddingValues) {
    navigation(
        route = UserProfileGraph.USER_PROFILE_ROOT,
        startDestination = UserProfileGraph.ADMIN_PANEL,
    ) {
        navigateWithFadeAnimation(route = UserProfileGraph.ADMIN_PANEL) {
            AdminPanelScreen(padding = padding)
        }
    }
}
