package com.ravenzip.devicepicker.ui.screens.main.user.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun UserSettingsScreenScaffold(padding: PaddingValues) {
    Scaffold(
        modifier = Modifier.padding(padding),
        topBar = { TopAppBar(title = "Настройки аккаунта") },
    ) { innerPadding ->
        UserSettingsScreenContent(innerPadding)
    }
}
