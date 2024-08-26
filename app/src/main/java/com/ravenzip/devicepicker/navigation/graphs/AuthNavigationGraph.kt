package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.ravenzip.devicepicker.extensions.functions.navigateWithFadeAnimation
import com.ravenzip.devicepicker.navigation.models.AuthGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.ui.screens.auth.ForgotPasswordScreen
import com.ravenzip.devicepicker.ui.screens.auth.LoginScreen
import com.ravenzip.devicepicker.ui.screens.auth.RegistrationScreen
import com.ravenzip.devicepicker.ui.screens.auth.WelcomeScreen
import com.ravenzip.devicepicker.ui.theme.SetWindowStyle
import com.ravenzip.devicepicker.viewmodels.UserViewModel

fun NavGraphBuilder.authNavigationGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
) {
    navigation(route = RootGraph.AUTHENTICATION, startDestination = AuthGraph.WELCOME) {
        navigateWithFadeAnimation(route = AuthGraph.WELCOME) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surface,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            WelcomeScreen(
                reloadUser = { userViewModel.reloadUser() },
                logInAnonymously = { userViewModel.logInAnonymously() },
                navigateToRegistrationScreen = { navController.navigate(AuthGraph.REGISTRATION) },
                navigateToLoginScreen = { navController.navigate(AuthGraph.LOGIN) },
                navigateToHomeScreen = { navigateToHome(navController) },
            )
        }
        navigateWithFadeAnimation(route = AuthGraph.REGISTRATION) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            RegistrationScreen(
                userViewModel = userViewModel,
                navigateToHomeScreen = { navigateToHome(navController) },
            )
        }
        navigateWithFadeAnimation(route = AuthGraph.LOGIN) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            LoginScreen(
                reloadUser = { userViewModel.reloadUser() },
                logInUserWithEmail = { email, password ->
                    userViewModel.logInUserWithEmail(email, password)
                },
                navigateToHomeScreen = { navigateToHome(navController) },
                navigateToForgotPassScreen = { navController.navigate(AuthGraph.FORGOT_PASS) },
            )
        }
        navigateWithFadeAnimation(route = AuthGraph.FORGOT_PASS) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme(),
            )

            ForgotPasswordScreen(
                reloadUser = { userViewModel.reloadUser() },
                sendPasswordResetEmail = { email -> userViewModel.sendPasswordResetEmail(email) },
            )
        }
    }
}

private fun navigateToHome(navController: NavHostController) {
    // Для того, чтобы перейти на главный экран и при этом невозможно было вернуться назад
    navController.navigate(RootGraph.MAIN) {
        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
    }
}
