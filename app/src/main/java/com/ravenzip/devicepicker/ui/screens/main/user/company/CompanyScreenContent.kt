package com.ravenzip.devicepicker.ui.screens.main.user.company

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.constants.enums.EmployeePosition
import com.ravenzip.devicepicker.extensions.functions.containerColor
import com.ravenzip.devicepicker.extensions.functions.inverseColors
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.model.CompanyDeleteRequest
import com.ravenzip.devicepicker.model.company.Company
import com.ravenzip.devicepicker.state.UiState
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.devicepicker.ui.screens.main.user.company.enums.CompanyScreenActionsEnum
import com.ravenzip.devicepicker.ui.screens.main.user.company.enums.CompanyScreenTypesEnum
import com.ravenzip.devicepicker.ui.theme.errorColor
import com.ravenzip.devicepicker.viewmodels.user.CompanyViewModel
import com.ravenzip.workshop.components.CustomButton
import com.ravenzip.workshop.components.DropDownTextField
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.MultilineTextField
import com.ravenzip.workshop.components.RadioGroup
import com.ravenzip.workshop.components.RowIconButton
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.components.Spinner
import com.ravenzip.workshop.data.TextConfig
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyScreenContent(viewModel: CompanyViewModel, padding: PaddingValues) {
    val screenTypeByUserState = viewModel.screenModeByUserState.collectAsStateWithLifecycle().value
    val companyState = viewModel.companyStateFlow.collectAsStateWithLifecycle().value

    when (companyState) {
        is UiState.Success -> {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                when (screenTypeByUserState) {
                    CompanyScreenTypesEnum.ANONYMOUS -> {
                        InfoCard(
                            text = "Отсутствует доступ",
                            description =
                                "Доступ к компаниям есть только у зарегистрированных пользователей",
                            icon = ImageVector.vectorResource(id = R.drawable.i_error),
                            iconColor = errorColor,
                        )
                    }

                    CompanyScreenTypesEnum.NOT_REGISTERED -> {
                        CompanyScreenStartPage(viewModel)
                    }

                    else -> {
                        CompanyScreenMainPage(viewModel = viewModel, company = companyState.data)
                    }
                }
            }
        }

        is UiState.Error -> {
            InfoCard(
                text = "Произошла ошибка",
                description =
                    "При загрузке данных произошла ошибка: ${companyState.message}. " +
                        "Пожалуйста, попробуйте позже",
                icon = ImageVector.vectorResource(id = R.drawable.i_error),
                iconColor = errorColor,
            )
        }

        is UiState.Loading -> {
            Spinner(companyState.message)
        }
    }
}

// TODO придумать наименование и, возможно, вынести в библиотеку
@Composable
private fun InfoCard(text: String, description: String, icon: ImageVector, iconColor: Color) {
    Card(modifier = Modifier.fillMaxWidth(0.9f), colors = CardDefaults.veryLightPrimary()) {
        Column(
            modifier = Modifier.padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(imageVector = icon, contentDescription = "", tint = iconColor)
                Text(text = text, fontSize = 22.sp, fontWeight = FontWeight.W500)
            }

            Text(text = description)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyScreenStartPage(viewModel: CompanyViewModel) {
    val composableScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        userScrollEnabled = false,
    ) { page ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            when (page) {
                0 -> {
                    RadioGroup(
                        state = viewModel.companyScreenTypeState,
                        source = CompanyScreenActionsEnum.entries,
                        view = { it.description },
                        comparableKey = { it },
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    InfoCard(
                        icon = IconData.ResourceIcon(R.drawable.i_info),
                        title = "Важно",
                        text =
                            "Перед началом работы с разделом \"Компания\" необходимо создать свою компанию, либо вступить в уже существующую.",
                        colors = CardDefaults.veryLightPrimary(),
                    )
                }

                1 -> {
                    when (viewModel.companyScreenTypeState.value) {
                        CompanyScreenActionsEnum.CREATE_COMPANY -> {
                            SinglenessOutlinedTextField(
                                viewModel.companyNameState,
                                label = "Наименование",
                            )
                            MultilineTextField(
                                viewModel.companyDescriptionState,
                                label = "Описание",
                            )
                            SinglenessOutlinedTextField(
                                viewModel.companyAddressState,
                                label = "Адрес",
                            )
                            SinglenessOutlinedTextField(
                                viewModel.companyCodeState,
                                label = "Код доступа",
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            InfoCard(
                                icon = IconData.ResourceIcon(R.drawable.i_info),
                                title = "Важно",
                                text =
                                    "Для создания компании заполните обязательные поля и нажмите кнопку \"Создать\". " +
                                        "После создания компании вы будете перенаправлены на главный экран вашей компании." +
                                        "Код необходим для авторизации сотрудников внутри вашей компании",
                                colors = CardDefaults.veryLightPrimary(),
                            )
                        }

                        CompanyScreenActionsEnum.JOIN_COMPANY -> {
                            DropDownTextField(viewModel.companyState, label = "Компания")
                            SinglenessOutlinedTextField(
                                viewModel.companyLeaderState,
                                label = "Руководитель",
                            )
                            SinglenessOutlinedTextField(
                                viewModel.companyAddressState,
                                label = "Адрес",
                            )
                            SinglenessOutlinedTextField(
                                viewModel.companyCodeState,
                                label = "Код доступа",
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            InfoCard(
                                icon = IconData.ResourceIcon(R.drawable.i_info),
                                title = "Важно",
                                text =
                                    "Для присоединения к уже существующей компании выберите компанию из списка, " +
                                        "введите код доступа и нажмите на кнопку \"Присоединиться\"",
                                colors = CardDefaults.veryLightPrimary(),
                            )
                        }
                    }

                    BackHandler { composableScope.launch { pagerState.animateScrollToPage(0) } }
                }
            }

            BottomContainer(padding = PaddingValues(top = 20.dp, bottom = 10.dp)) {
                if (page == 1) {
                    SimpleButton(text = "Назад", colors = ButtonDefaults.inverseColors()) {
                        composableScope.launch { pagerState.animateScrollToPage(0) }
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                }

                if (page == 0) {
                    SimpleButton(text = "Продолжить") {
                        composableScope.launch { pagerState.animateScrollToPage(1) }
                    }
                } else {
                    when (viewModel.companyScreenTypeState.value) {
                        CompanyScreenActionsEnum.CREATE_COMPANY -> {
                            SimpleButton(text = "Создать") {
                                composableScope.launch { viewModel.createCompany.emit(Unit) }
                            }
                        }

                        CompanyScreenActionsEnum.JOIN_COMPANY -> {
                            SimpleButton(text = "Присоединиться") {
                                composableScope.launch { viewModel.joinToCompany.emit(Unit) }
                            }
                        }
                    }
                }
            }
        }
    }
}

// TODO разработать основной экран компании
@Composable
private fun CompanyScreenMainPage(viewModel: CompanyViewModel, company: Company) {
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

                        Text("Наименование: ${company.name}")
                        Text("Ваша роль: ${currentUserPositionInCompany.description}")
                        Text("Количество участников: $employeesCount")
                    }
                }

                if (currentUserPositionInCompany == EmployeePosition.Leader) {
                    CustomButton(
                        title = "Сотрудники",
                        titleConfig = TextConfig.onSurfaceH2,
                        text = "Просмотр списка сотрудников и привязанных к ним устройств",
                        textConfig = TextConfig.onSurface85Small,
                        icon = IconData.ResourceIcon(R.drawable.i_employee),
                        iconConfig = IconConfig.Primary,
                        colors = ButtonDefaults.veryLightPrimary(),
                    )

                    CustomButton(
                        title = "Устройства",
                        titleConfig = TextConfig.onSurfaceH2,
                        text = "Просмотр устройств, которые привязаны к компании",
                        textConfig = TextConfig.onSurface85Small,
                        icon = IconData.ResourceIcon(R.drawable.i_devices),
                        iconConfig = IconConfig.Primary,
                        colors = ButtonDefaults.veryLightPrimary(),
                    )
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
            InfoCard(
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

// TODO убрать после обновления WorkShop
class SpinnerState(val isLoading: Boolean, val text: String)
