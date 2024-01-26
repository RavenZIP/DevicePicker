package com.ravenzip.devicepicker.navigation.auth

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ravenzip.devicepicker.auth.ForgotPasswordScreen
import com.ravenzip.devicepicker.auth.LoginScreen
import com.ravenzip.devicepicker.auth.RegistrationScreen
import com.ravenzip.devicepicker.auth.WelcomeScreen
import com.ravenzip.devicepicker.navigation.root.RootGraph
import com.ravenzip.devicepicker.ui.theme.setWindowStyle

fun NavGraphBuilder.authNavigationGraph(navController: NavHostController) {
    navigation(route = RootGraph.AUTHENTICATION, startDestination = AuthGraph.WELCOME) {
        composable(route = AuthGraph.WELCOME) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surface,
                isAppearanceLight = !isSystemInDarkTheme()
            )

            WelcomeScreen(
                registrationClick = { navController.navigate(AuthGraph.REGISTRATION) },
                loginClick = { navController.navigate(AuthGraph.LOGIN) },
                continueWithoutAuthClick = { navController.navigate(RootGraph.MAIN) }
            )
        }
        composable(route = AuthGraph.REGISTRATION) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                isAppearanceLight = !isSystemInDarkTheme()
            )

            RegistrationScreen()
        }
        composable(route = AuthGraph.LOGIN) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                isAppearanceLight = !isSystemInDarkTheme()
            )

            LoginScreen(forgotPassClick = { navController.navigate(AuthGraph.FORGOT_PASS) })
        }
        composable(route = AuthGraph.FORGOT_PASS) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                isAppearanceLight = !isSystemInDarkTheme()
            )

            ForgotPasswordScreen()
        }
    }
}
