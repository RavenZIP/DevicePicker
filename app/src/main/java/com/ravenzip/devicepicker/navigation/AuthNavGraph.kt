package com.ravenzip.devicepicker.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ravenzip.devicepicker.auth.ForgotPasswordScreen
import com.ravenzip.devicepicker.auth.LoginScreen
import com.ravenzip.devicepicker.auth.RegistrationScreen
import com.ravenzip.devicepicker.auth.WelcomeScreen

fun NavGraphBuilder.authNavigationGraph(navController: NavHostController) {
    navigation(route = Graph.AUTHENTICATION, startDestination = AuthScreen.Welcome.route) {
        composable(route = AuthScreen.Welcome.route) {
            WelcomeScreen(
                registrationClick = { navController.navigate(AuthScreen.Registration.route) },
                loginClick = { navController.navigate(AuthScreen.Login.route) },
                forgotPassClick = { navController.navigate(AuthScreen.ForgotPass.route) }
            )
        }
        composable(route = AuthScreen.Registration.route) { RegistrationScreen() }
        composable(route = AuthScreen.Login.route) { LoginScreen() }
        composable(route = AuthScreen.ForgotPass.route) { ForgotPasswordScreen() }
    }
}

private sealed class AuthScreen(val route: String) {
    data object Welcome : AuthScreen(route = "WELCOME")

    data object Registration : AuthScreen(route = "REGISTRATION")

    data object Login : AuthScreen(route = "LOGIN")

    data object ForgotPass : AuthScreen(route = "FORGOT PASS")
}
