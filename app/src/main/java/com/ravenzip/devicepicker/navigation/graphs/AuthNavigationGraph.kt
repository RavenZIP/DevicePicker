package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.extensions.functions.composable
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.navigation.models.AuthGraph
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.ui.screens.auth.ForgotPasswordScreen
import com.ravenzip.devicepicker.ui.screens.auth.LoginScreen
import com.ravenzip.devicepicker.ui.screens.auth.RegistrationScreen
import com.ravenzip.devicepicker.ui.screens.auth.WelcomeScreen
import com.ravenzip.devicepicker.ui.theme.SetWindowStyle
import kotlinx.coroutines.flow.Flow

fun NavGraphBuilder.authNavigationGraph(
    navController: NavHostController,
    reloadUser: suspend () -> Result<Boolean>,
    logInAnonymously: suspend () -> Result<AuthResult>,
    createUserWithEmail: suspend (email: String, password: String) -> Result<AuthResult>,
    sendEmailVerification: suspend () -> Result<Boolean>,
    deleteAccount: suspend () -> Result<Boolean>,
    isEmailVerified: suspend () -> Boolean,
    addUserData: suspend (user: FirebaseUser?) -> Flow<Boolean>,
    logInUserWithEmail: suspend (email: String, password: String) -> Result<AuthResult>,
    sendPasswordResetEmail: suspend (email: String) -> Result<Boolean>,
    getUser: () -> FirebaseUser?,
) {
    navigation(route = RootGraph.AUTHENTICATION, startDestination = AuthGraph.WELCOME) {
        composable(route = AuthGraph.WELCOME) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surface,
                isAppearanceLight = !isSystemInDarkTheme())

            WelcomeScreen(
                reloadUser = reloadUser,
                logInAnonymously = logInAnonymously,
                navigateToRegistrationScreen = { navController.navigate(AuthGraph.REGISTRATION) },
                navigateToLoginScreen = { navController.navigate(AuthGraph.LOGIN) },
                navigateToHomeScreen = { navigateToHome(navController) })
        }
        composable(route = AuthGraph.REGISTRATION) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme())

            RegistrationScreen(
                reloadUser = reloadUser,
                createUserWithEmail = createUserWithEmail,
                sendEmailVerification = sendEmailVerification,
                deleteAccount = deleteAccount,
                isEmailVerified = isEmailVerified,
                addUserData = addUserData,
                getUser = getUser,
                navigateToHomeScreen = { navigateToHome(navController) })
        }
        composable(route = AuthGraph.LOGIN) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme())

            LoginScreen(
                reloadUser = reloadUser,
                logInUserWithEmail = logInUserWithEmail,
                navigateToHomeScreen = { navigateToHome(navController) },
                navigateToForgotPassScreen = { navController.navigate(AuthGraph.FORGOT_PASS) })
        }
        composable(route = AuthGraph.FORGOT_PASS) {
            SetWindowStyle(
                view = LocalView.current,
                statusBarColor = MaterialTheme.colorScheme.surface,
                navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                isAppearanceLight = !isSystemInDarkTheme())

            ForgotPasswordScreen(
                reloadUser = reloadUser, sendPasswordResetEmail = sendPasswordResetEmail)
        }
    }
}

private fun navigateToHome(navController: NavHostController) {
    // Для того, чтобы перейти на главный экран и при этом невозможно было вернуться назад
    navController.navigate(RootGraph.MAIN) {
        popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
    }
}
