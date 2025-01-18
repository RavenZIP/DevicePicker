package com.ravenzip.devicepicker.ui.screens.main.user.company

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun CompanyScreenScaffold(padding: PaddingValues) {
    Scaffold(modifier = Modifier.padding(padding), topBar = { TopAppBar(title = "Компания") }) {
        innerPadding ->
        CompanyScreenContent(innerPadding)
    }
}
