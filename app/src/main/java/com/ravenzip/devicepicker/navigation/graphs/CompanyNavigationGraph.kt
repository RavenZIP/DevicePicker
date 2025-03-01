package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.extensions.functions.navigateWithSlideAnimation
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.devicepicker.ui.screens.main.user.company.create.CompanyScreenCreateScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.company.info.CompanyInfoScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.company.join.CompanyScreenJoinScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.company.root.CompanyRootScreenScaffold

@Composable
fun CompanyNavigationGraph(
    navController: NavHostController = rememberNavController(),
    navigateToUserProfile: () -> Unit,
    padding: PaddingValues,
) {
    // TODO есть большая проблема с созданием экранов из-за текущего принципа навигации, необходимо
    // срочно доработать
    NavHost(
        navController = navController,
        route = BottomBarGraph.USER_PROFILE,
        startDestination = CompanyGraph.COMPANY_ROOT,
    ) {
        navigateWithSlideAnimation(route = CompanyGraph.COMPANY_ROOT) {
            CompanyRootScreenScaffold(
                padding = padding,
                navigateTo = { route -> navController.navigate(route) },
                navigateBack = { navigateToUserProfile() },
            )
        }

        navigateWithSlideAnimation(route = CompanyGraph.CREATE_COMPANY) {
            CompanyScreenCreateScaffold(
                padding = padding,
                navigateTo = { route -> navController.navigate(route) },
                navigateBack = { navigateToUserProfile() },
            )
        }

        navigateWithSlideAnimation(route = CompanyGraph.JOIN_TO_COMPANY) {
            CompanyScreenJoinScaffold(
                padding = padding,
                navigateTo = { route -> navController.navigate(route) },
                navigateBack = { navigateToUserProfile() },
            )
        }

        navigateWithSlideAnimation(route = "${CompanyGraph.COMPANY_INFO}/{uid}") {
            CompanyInfoScreenScaffold(
                padding = padding,
                navigateTo = { route -> navController.navigate(route) },
                navigateBack = { navigateToUserProfile() },
            )
        }

        navigateWithSlideAnimation(route = CompanyGraph.COMPANY_USERS) {}

        navigateWithSlideAnimation(route = CompanyGraph.COMPANY_DEVICES) {}
    }
}
