package com.ravenzip.devicepicker.navigation

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
import com.ravenzip.devicepicker.ui.theme.setWindowStyle

fun NavGraphBuilder.authNavigationGraph(navController: NavHostController) {
    navigation(route = Graph.AUTHENTICATION, startDestination = AuthScreen.Welcome.route) {
        composable(route = AuthScreen.Welcome.route) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surface,
                isAppearanceLight = isSystemInDarkTheme()
            )

            WelcomeScreen(
                registrationClick = { navController.navigate(AuthScreen.Registration.route) },
                loginClick = { navController.navigate(AuthScreen.Login.route) },
                continueWithoutAuthClick = { navController.navigate(Graph.MAIN) }
            )
        }
        composable(route = AuthScreen.Registration.route) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                isAppearanceLight = isSystemInDarkTheme()
            )

            RegistrationScreen()
        }
        composable(route = AuthScreen.Login.route) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                isAppearanceLight = isSystemInDarkTheme()
            )

            LoginScreen(forgotPassClick = { navController.navigate(AuthScreen.ForgotPass.route) })
        }
        composable(route = AuthScreen.ForgotPass.route) {
            setWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                isAppearanceLight = isSystemInDarkTheme()
            )

            ForgotPasswordScreen()
        }
    }
}

private sealed class AuthScreen(val route: String) {
    data object Welcome : AuthScreen(route = "WELCOME")

    data object Registration : AuthScreen(route = "REGISTRATION")

    data object Login : AuthScreen(route = "LOGIN")

    data object ForgotPass : AuthScreen(route = "FORGOT PASS")
}
