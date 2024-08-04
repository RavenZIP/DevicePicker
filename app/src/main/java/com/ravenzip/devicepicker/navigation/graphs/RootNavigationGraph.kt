package com.ravenzip.devicepicker.navigation.graphs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.model.result.Result
import com.ravenzip.devicepicker.navigation.models.RootGraph
import com.ravenzip.devicepicker.ui.screens.main.ScaffoldScreen
import com.ravenzip.devicepicker.ui.theme.SetWindowStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    startDestination: String,
    reloadUser: suspend () -> Result<Boolean>,
    logInAnonymously: suspend () -> Result<AuthResult>,
    createUserWithEmail: suspend (email: String, password: String) -> Result<AuthResult>,
    sendEmailVerification: suspend () -> Result<Boolean>,
    deleteAccount: suspend () -> Result<Boolean>,
    isEmailVerified: suspend () -> Boolean,
    logInUserWithEmail: suspend (email: String, password: String) -> Result<AuthResult>,
    sendPasswordResetEmail: suspend (email: String) -> Result<Boolean>,
    addUserData: suspend (user: FirebaseUser?) -> Flow<Boolean>,
    userDataByViewModel: StateFlow<User>,
    getUser: () -> FirebaseUser?,
    getUserData: suspend (user: FirebaseUser?) -> Flow<User>,
    logout: suspend () -> Unit,
) {

    NavHost(
        navController = navController,
        route = RootGraph.ROOT,
        startDestination = startDestination) {
            authNavigationGraph(
                navController = navController,
                reloadUser = reloadUser,
                logInAnonymously = logInAnonymously,
                createUserWithEmail = createUserWithEmail,
                sendEmailVerification = sendEmailVerification,
                deleteAccount = deleteAccount,
                isEmailVerified = isEmailVerified,
                addUserData = addUserData,
                logInUserWithEmail = logInUserWithEmail,
                sendPasswordResetEmail = sendPasswordResetEmail,
                getUser = getUser)

            composable(route = RootGraph.MAIN) {
                SetWindowStyle(
                    view = LocalView.current,
                    statusBarColor = MaterialTheme.colorScheme.surface,
                    navigationBarColor = MaterialTheme.colorScheme.surfaceContainer,
                    isAppearanceLight = !isSystemInDarkTheme())

                ScaffoldScreen(
                    userDataByViewModel = userDataByViewModel,
                    getUser = getUser,
                    getUserData = getUserData,
                    logout = logout)
            }
        }
}
