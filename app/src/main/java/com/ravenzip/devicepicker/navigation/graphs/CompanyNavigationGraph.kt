package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ravenzip.devicepicker.common.utils.extension.navigateWithFadeAnimation
import com.ravenzip.devicepicker.common.utils.extension.navigateWithSlideAnimation
import com.ravenzip.devicepicker.features.main.device.info.DeviceInfoScreenScaffold
import com.ravenzip.devicepicker.features.main.user.company.screens.create.CompanyScreenCreateScaffold
import com.ravenzip.devicepicker.features.main.user.company.screens.devices.DevicesCompanyScreenScaffold
import com.ravenzip.devicepicker.features.main.user.company.screens.devices.add.AddDevicesToCompanyScreenScaffold
import com.ravenzip.devicepicker.features.main.user.company.screens.employees.EmployeesCompanyScreenScaffold
import com.ravenzip.devicepicker.features.main.user.company.screens.employees.employee.EmployeeCompanyScreenScaffold
import com.ravenzip.devicepicker.features.main.user.company.screens.info.CompanyInfoScreenScaffold
import com.ravenzip.devicepicker.features.main.user.company.screens.info.CompanyInfoViewModel
import com.ravenzip.devicepicker.features.main.user.company.screens.join.CompanyScreenJoinScaffold
import com.ravenzip.devicepicker.features.main.user.company.screens.root.CompanyRootScreenScaffold
import com.ravenzip.devicepicker.features.main.user.company.screens.settings.CompanySettingsScreenScaffold
import com.ravenzip.devicepicker.navigation.NavigationParams
import com.ravenzip.devicepicker.navigation.models.BottomBarGraph
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.devicepicker.navigation.models.HomeGraph

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
                navigateToEmployee = { employeeUid ->
                    navController.navigate("${CompanyGraph.COMPANY_EMPLOYEES}/${employeeUid}")
                },
                navigateBack = { navController.popBackStack() },
            )
        }

        navigateWithSlideAnimation(route = "${CompanyGraph.COMPANY_EMPLOYEES}/{uid}") {
            val viewModel: CompanyInfoViewModel =
                hiltViewModel(navController.getBackStackEntry("${CompanyGraph.COMPANY_INFO}/{uid}"))

            EmployeeCompanyScreenScaffold(
                viewModel = viewModel,
                padding = padding,
                navigateBack = { navController.popBackStack() },
            )
        }

        navigateWithSlideAnimation(route = CompanyGraph.COMPANY_DEVICES) {
            val viewModel: CompanyInfoViewModel =
                hiltViewModel(navController.getBackStackEntry("${CompanyGraph.COMPANY_INFO}/{uid}"))

            DevicesCompanyScreenScaffold(
                companyInfoViewModel = viewModel,
                padding = padding,
                navigateBack = { navController.popBackStack() },
                navigateTo = { route -> navController.navigate(route) },
            )
        }

        navigateWithSlideAnimation(route = CompanyGraph.COMPANY_SETTINGS) {
            val viewModel: CompanyInfoViewModel =
                hiltViewModel(navController.getBackStackEntry("${CompanyGraph.COMPANY_INFO}/{uid}"))

            CompanySettingsScreenScaffold(
                viewModel = viewModel,
                padding = padding,
                navigateBack = { navController.popBackStack() },
            )
        }

        navigateWithSlideAnimation(route = CompanyGraph.ADD_DEVICES) {
            AddDevicesToCompanyScreenScaffold(
                padding = padding,
                navigateBack = { navController.popBackStack() },
                navigateTo = { route -> navController.navigate(route) },
            )
        }

        navigateWithFadeAnimation(route = "${HomeGraph.DEVICE_INFO}/{uid}") {
            DeviceInfoScreenScaffold(
                padding = padding,
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}
