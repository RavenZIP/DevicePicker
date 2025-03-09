package com.ravenzip.devicepicker.features.main.user.settings.visual

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun VisualAppearanceScreenScaffold(padding: PaddingValues) {
    Scaffold(modifier = Modifier.padding(padding), topBar = { TopAppBar(title = "Настройки") }) {
        innerPadding ->
        VisualAppearanceScreenContent(innerPadding)
    }
}
