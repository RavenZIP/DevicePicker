package com.ravenzip.devicepicker.ui.screens.main.user.company.employees

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.ui.components.SmallText
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CompanyInfoViewModel

// TODO подумать, нужна ли все-таки отдельная viewModel или нет
@Composable
fun EmployeesCompanyScreenContent(viewModel: CompanyInfoViewModel) {
    val company = viewModel.company.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(company.employees) { employee ->
            Card(modifier = Modifier.fillMaxWidth(0.9f), colors = CardDefaults.veryLightPrimary()) {
                Column(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
                    SmallText(text = "ФИО", fontWeight = FontWeight.W500, letterSpacing = 0.sp)
                    SmallText(text = employee.name, letterSpacing = 0.sp)

                    Spacer(modifier = Modifier.padding(10.dp))

                    SmallText(
                        text = "Должность",
                        fontWeight = FontWeight.W500,
                        letterSpacing = 0.sp,
                    )
                    SmallText(text = employee.position.description, letterSpacing = 0.sp)

                    Spacer(modifier = Modifier.padding(10.dp))

                    SmallText(
                        text = "Текущее устройство: ???",
                        fontWeight = FontWeight.W500,
                        letterSpacing = 0.sp,
                    )
                    SmallText(
                        text = "тут карточка с устройством (прям мини-мини карточка)",
                        letterSpacing = 0.sp,
                    )
                }
            }
        }
    }
}
