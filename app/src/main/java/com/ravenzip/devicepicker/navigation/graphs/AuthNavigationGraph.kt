package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.ravenzip.devicepicker.common.theme.SetWindowStyle
import com.ravenzip.devicepicker.common.utils.extension.navigateWithFadeAnimation
import com.ravenzip.devicepicker.common.utils.extension.navigateWithSlideAnimation
import com.ravenzip.devicepicker.common.utils.extension.navigateWithoutPreviousRoute
import com.ravenzip.devicepicker.features.auth.forgot.password.ForgotPasswordScreen
import com.ravenzip.devicepicker.features.auth.login.LoginScreen
import com.ravenzip.devicepicker.features.auth.registration.RegistrationScreen
import com.ravenzip.devicepicker.features.auth.welcome.WelcomeScreen
import com.ravenzip.devicepicker.navigation.models.AuthGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph

fun NavGraphBuilder.authNavigationGraph(navController: NavHostController) {
    navigation(route = RootGraph.AUTHENTICATION, startDestination = AuthGraph.WELCOME) {
        navigateWithFadeAnimation(route = AuthGraph.WELCOME) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surface,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            WelcomeScreen(
                navigateToRegistrationScreen = { navController.navigate(AuthGraph.REGISTRATION) },
                navigateToLoginScreen = { navController.navigate(AuthGraph.LOGIN) },
                navigateToHomeScreen = {
                    navController.navigateWithoutPreviousRoute(
                        startDestination = RootGraph.ROOT,
                        targetDestination = RootGraph.MAIN,
                    )
                },
            )
        }

        navigateWithSlideAnimation(route = AuthGraph.REGISTRATION) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            RegistrationScreen(
                navigateToHomeScreen = {
                    navController.navigateWithoutPreviousRoute(
                        startDestination = RootGraph.ROOT,
                        targetDestination = RootGraph.MAIN,
                    )
                }
            )
        }

        navigateWithSlideAnimation(route = AuthGraph.LOGIN) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            LoginScreen(
                navigateToHomeScreen = {
                    navController.navigateWithoutPreviousRoute(
                        startDestination = RootGraph.ROOT,
                        targetDestination = RootGraph.MAIN,
                    )
                },
                navigateToForgotPassScreen = { navController.navigate(AuthGraph.FORGOT_PASS) },
            )
        }

        navigateWithSlideAnimation(route = AuthGraph.FORGOT_PASS) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            ForgotPasswordScreen()
        }
    }
}
