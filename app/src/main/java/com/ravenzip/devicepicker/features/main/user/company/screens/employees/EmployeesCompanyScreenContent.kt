package com.ravenzip.devicepicker.features.main.user.company.screens.employees

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.common.components.SmallText
import com.ravenzip.devicepicker.common.utils.extension.veryLightPrimary
import com.ravenzip.devicepicker.features.main.user.company.screens.info.CompanyInfoViewModel

// TODO подумать, нужна ли все-таки отдельная viewModel или нет
@Composable
fun EmployeesCompanyScreenContent(
    viewModel: CompanyInfoViewModel,
    navigateToEmployee: (route: String) -> Unit,
) {
    val employees = viewModel.employeesWithActiveDevices.collectAsStateWithLifecycle().value

    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(employees) { employeeWithDevices ->
            Card(
                modifier =
                    Modifier.fillMaxWidth(0.9f).clip(CardDefaults.shape).clickable {
                        navigateToEmployee(employeeWithDevices.employee.uid)
                    },
                colors = CardDefaults.veryLightPrimary(),
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(15.dp)) {
                    SmallText(text = "ФИО", fontWeight = FontWeight.W500, letterSpacing = 0.sp)
                    SmallText(text = employeeWithDevices.employee.name, letterSpacing = 0.sp)

                    Spacer(modifier = Modifier.padding(5.dp))

                    SmallText(
                        text = "Должность",
                        fontWeight = FontWeight.W500,
                        letterSpacing = 0.sp,
                    )
                    SmallText(
                        text = employeeWithDevices.employee.position.description,
                        letterSpacing = 0.sp,
                    )

                    Spacer(modifier = Modifier.padding(5.dp))

                    SmallText(
                        text = "Количество используемых устройств",
                        fontWeight = FontWeight.W500,
                        letterSpacing = 0.sp,
                    )
                    SmallText(
                        text =
                            "${if (employeeWithDevices.devices.isEmpty()) "У пользователя отсутствуют используемые устройства" 
                            else employeeWithDevices.devices.count()}",
                        letterSpacing = 0.sp,
                    )
                }
            }
        }
    }
}
