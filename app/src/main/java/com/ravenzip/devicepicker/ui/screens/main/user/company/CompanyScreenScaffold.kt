package com.ravenzip.devicepicker.ui.screens.main.user.company

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravenzip.devicepicker.viewmodels.user.CompanyViewModel
import com.ravenzip.workshop.components.TopAppBar

@Composable
fun CompanyScreenScaffold(viewModel: CompanyViewModel = hiltViewModel(), padding: PaddingValues) {
    Scaffold(modifier = Modifier.padding(padding), topBar = { TopAppBar(title = "Компания") }) {
        innerPadding ->
        CompanyScreenContent(viewModel, innerPadding)
    }
}
