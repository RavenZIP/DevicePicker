package com.ravenzip.devicepicker.ui.screens.main.user.company.employees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.state.UiState
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CompanyInfoViewModel

@Composable
fun EmployeesCompanyScreenContent(viewModel: CompanyInfoViewModel) {
    val company = (viewModel.company.collectAsStateWithLifecycle().value as UiState.Success).data
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(company.employees) { employee ->
            Card(modifier = Modifier.fillMaxWidth(0.9f), colors = CardDefaults.veryLightPrimary()) {
                Column { Text(text = employee.name) }
            }
        }
    }
}
