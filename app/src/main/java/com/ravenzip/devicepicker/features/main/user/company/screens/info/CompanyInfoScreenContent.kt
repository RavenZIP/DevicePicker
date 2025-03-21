package com.ravenzip.devicepicker.features.main.user.company.screens.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.ErrorScreenCard
import com.ravenzip.devicepicker.common.enums.EmployeePositionEnum
import com.ravenzip.devicepicker.common.theme.errorColor
import com.ravenzip.devicepicker.common.utils.extension.containerColor
import com.ravenzip.devicepicker.common.utils.extension.veryLightPrimary
import com.ravenzip.devicepicker.features.main.user.company.model.Company
import com.ravenzip.devicepicker.features.main.user.company.model.CompanyDeleteRequest
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.workshop.components.CustomButton
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData
import kotlinx.coroutines.launch

@Composable
fun CompanyInfoScreenContent(viewModel: CompanyInfoViewModel, company: Company) {
    val currentUserPositionInCompany =
        viewModel.currentUserPositionInCompany.collectAsStateWithLifecycle().value
    val employeesCount = viewModel.employeesCount.collectAsStateWithLifecycle().value
    val composableScope = rememberCoroutineScope()

    when (currentUserPositionInCompany) {
        EmployeePositionEnum.Leader,
        EmployeePositionEnum.Employee -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = CardDefaults.veryLightPrimary(),
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Text(
                            text = "Краткие сведения",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.W500,
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Наименование: ${company.name}", letterSpacing = 0.sp)
                        Text(
                            "Ваша роль: ${currentUserPositionInCompany.description}",
                            letterSpacing = 0.sp,
                        )
                        Text("Количество участников: $employeesCount", letterSpacing = 0.sp)
                    }
                }

                CustomButton(
                    title = "Сотрудники",
                    titleConfig = TextConfig.onSurfaceH2,
                    text = "Просмотр списка сотрудников и привязанных к ним устройств",
                    textConfig = TextConfig.onSurface85Small,
                    icon = IconData.ResourceIcon(R.drawable.i_employee),
                    iconConfig = IconConfig.Primary,
                    colors = ButtonDefaults.veryLightPrimary(),
                ) {
                    composableScope.launch {
                        viewModel.navigateTo.emit(CompanyGraph.COMPANY_EMPLOYEES)
                    }
                }

                if (currentUserPositionInCompany == EmployeePositionEnum.Leader) {
                    CustomButton(
                        title = "Устройства",
                        titleConfig = TextConfig.onSurfaceH2,
                        text = "Просмотр устройств, которые привязаны к компании",
                        textConfig = TextConfig.onSurface85Small,
                        icon = IconData.ResourceIcon(R.drawable.i_devices),
                        iconConfig = IconConfig.Primary,
                        colors = ButtonDefaults.veryLightPrimary(),
                    ) {
                        composableScope.launch {
                            viewModel.navigateTo.emit(CompanyGraph.COMPANY_DEVICES)
                        }
                    }

                    RowIconButton(
                        text = "Настройки",
                        textConfig = TextConfig.onSurfaceH2,
                        icon = IconData.ResourceIcon(R.drawable.i_settings),
                        iconConfig = IconConfig.Primary,
                        colors = ButtonDefaults.veryLightPrimary(),
                    ) {
                        composableScope.launch {
                            viewModel.navigateTo.emit(CompanyGraph.COMPANY_SETTINGS)
                        }
                    }
                }

                RowIconButton(
                    text = "Покинуть компанию",
                    textConfig = TextConfig.onSurfaceH2,
                    icon = IconData.ResourceIcon(R.drawable.sign_in),
                    iconConfig = IconConfig(color = errorColor),
                    colors = ButtonDefaults.containerColor(),
                ) {
                    composableScope.launch {
                        viewModel.leaveCompany.emit(
                            CompanyDeleteRequest(
                                company.uid,
                                currentUserPositionInCompany,
                                company.employees,
                            )
                        )
                    }
                }
            }
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                ErrorScreenCard(
                    text = "Произошла ошибка",
                    description =
                        "При получении сведений о пользователе произошла ошибка. " +
                            "Нарушена целостность данных",
                )
            }
        }
    }
}
