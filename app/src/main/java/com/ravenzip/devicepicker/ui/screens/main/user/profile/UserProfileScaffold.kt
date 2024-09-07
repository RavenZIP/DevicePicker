package com.ravenzip.devicepicker.ui.screens.main.user.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ravenzip.devicepicker.viewmodels.main.UserProfileViewModel
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun UserProfileScaffold(
    userProfileViewModel: UserProfileViewModel,
    onClickToAdminPanel: () -> Unit,
    onClickToDeviceHistory: () -> Unit,
    navigateToSplashScreen: () -> Unit,
    padding: PaddingValues,
) {
    Scaffold(modifier = Modifier.padding(padding), topBar = { TopAppBar("Профиль") }) { innerPadding
        ->
        UserProfileScreenContent(
            userProfileViewModel = userProfileViewModel,
            onClickToAdminPanel = onClickToAdminPanel,
            onClickToDeviceHistory = onClickToDeviceHistory,
            navigateToSplashScreen = navigateToSplashScreen,
            padding = innerPadding,
        )
    }
}
