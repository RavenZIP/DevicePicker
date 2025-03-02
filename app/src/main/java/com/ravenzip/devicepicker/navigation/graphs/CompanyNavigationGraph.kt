package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.extensions.functions.navigateWithSlideAnimation
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.devicepicker.state.NavigationParams
import com.ravenzip.devicepicker.ui.screens.main.user.company.create.CompanyScreenCreateScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.company.devices.DevicesCompanyScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.company.employees.EmployeesCompanyScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.company.info.CompanyInfoScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.company.join.CompanyScreenJoinScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.company.root.CompanyRootScreenScaffold
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CompanyInfoViewModel

@Composable
fun CompanyNavigationGraph(
    navController: NavHostController = rememberNavController(),
    navigateToUserProfile: () -> Unit,
    padding: PaddingValues,
) {
    NavHost(
        navController = navController,
        route = BottomBarGraph.USER_PROFILE,
        startDestination = CompanyGraph.COMPANY_ROOT,
    ) {
        navigateWithSlideAnimation(route = CompanyGraph.COMPANY_ROOT) {
            CompanyRootScreenScaffold(
                padding = padding,
                navigationParams =
                    NavigationParams.fromNavController(
                        navController = navController,
                        navigateBackToParent = navigateToUserProfile,
                    ),
            )
        }

        navigateWithSlideAnimation(route = CompanyGraph.CREATE_COMPANY) {
            CompanyScreenCreateScaffold(
                padding = padding,
                navigationParams =
                    NavigationParams.fromNavController(
                        navController = navController,
                        navigateBackToParent = navigateToUserProfile,
                    ),
            )
        }

        navigateWithSlideAnimation(route = CompanyGraph.JOIN_TO_COMPANY) {
            CompanyScreenJoinScaffold(
                padding = padding,
                navigationParams =
                    NavigationParams.fromNavController(
                        navController = navController,
                        navigateBackToParent = navigateToUserProfile,
                    ),
            )
        }

        navigateWithSlideAnimation(route = "${CompanyGraph.COMPANY_INFO}/{uid}") {
            CompanyInfoScreenScaffold(
                padding = padding,
                navigationParams =
                    NavigationParams.fromNavController(
                        navController = navController,
                        navigateBackToParent = navigateToUserProfile,
                    ),
            )
        }

        navigateWithSlideAnimation(route = CompanyGraph.COMPANY_EMPLOYEES) {
            val viewModel: CompanyInfoViewModel =
                hiltViewModel(navController.getBackStackEntry("${CompanyGraph.COMPANY_INFO}/{uid}"))

            EmployeesCompanyScreenScaffold(
                viewModel = viewModel,
                padding = padding,
                navigateBack = { navController.popBackStack() },
            )
        }

        navigateWithSlideAnimation(route = CompanyGraph.COMPANY_DEVICES) {
            val viewModel: CompanyInfoViewModel =
                hiltViewModel(navController.getBackStackEntry("${CompanyGraph.COMPANY_INFO}/{uid}"))

            DevicesCompanyScreenScaffold(
                viewModel = viewModel,
                padding = padding,
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}
