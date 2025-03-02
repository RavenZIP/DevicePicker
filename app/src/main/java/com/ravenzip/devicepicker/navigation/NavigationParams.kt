package com.ravenzip.devicepicker.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavController

@Stable
data class NavigationParams(
    val navigateToWithClearBackStack: (route: String) -> Unit = {},
    val navigateTo: (route: String) -> Unit = {},
    val navigateBack: () -> Unit = {},
    val navigateBackToParent: () -> Unit = {},
) {
    companion object {
        fun fromNavController(
            navController: NavController,
            navigateBackToParent: () -> Unit = {},
        ): NavigationParams {
            return NavigationParams(
                navigateToWithClearBackStack = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                navigateTo = { route -> navController.navigate(route) },
                navigateBack = { navController.popBackStack() },
                navigateBackToParent = { navigateBackToParent() },
            )
        }
    }
}
