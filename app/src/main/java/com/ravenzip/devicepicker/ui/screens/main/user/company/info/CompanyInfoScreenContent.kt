package com.ravenzip.devicepicker.ui.screens.main.user.company.info

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.EmployeePosition
import com.ravenzip.devicepicker.extensions.functions.containerColor
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.model.CompanyDeleteRequest
import com.ravenzip.devicepicker.model.company.Company
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.devicepicker.ui.screens.main.user.company.InfoCard2
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CompanyInfoViewModel
import com.ravenzip.devicepicker.ui.theme.errorColor
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
        EmployeePosition.Leader,
        EmployeePosition.Employee -> {
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
                    Column(modifier = Modifier.padding(10.dp)) {
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

                if (currentUserPositionInCompany == EmployeePosition.Leader) {
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
                InfoCard2(
                    text = "Произошла ошибка",
                    description =
                        "При получении сведений о пользователе произошла ошибка. " +
                            "Нарушена целостность данных",
                    icon = ImageVector.vectorResource(id = R.drawable.i_error),
                    iconColor = errorColor,
                )
            }
        }
    }
}
