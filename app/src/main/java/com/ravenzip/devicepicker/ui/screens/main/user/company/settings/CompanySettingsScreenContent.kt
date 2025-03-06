package com.ravenzip.devicepicker.ui.screens.main.user.company.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CompanyInfoViewModel

@Composable
fun CompanySettingsScreenContent(viewModel: CompanyInfoViewModel) {
    val company = viewModel.company.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(company.settings) { setting ->
            // TODO нужны доработки из WorkShop с новым контролом множественного выбора
        }
    }
}
