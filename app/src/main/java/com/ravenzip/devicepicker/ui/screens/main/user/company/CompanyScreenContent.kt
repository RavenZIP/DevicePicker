package com.ravenzip.devicepicker.ui.screens.main.user.company

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.extensions.functions.inverseColors
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.devicepicker.ui.components.ScreenTitle
import com.ravenzip.devicepicker.viewmodels.user.CompanyViewModel
import com.ravenzip.workshop.components.DropDownTextField
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.MultilineTextField
import com.ravenzip.workshop.components.RadioGroup
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.data.icon.Icon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyScreenContent(viewModel: CompanyViewModel, padding: PaddingValues) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val companyUid = viewModel.companyUid.collectAsStateWithLifecycle().value
    val composableScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
        if (companyUid.isEmpty()) {
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
                    Spacer(modifier = Modifier.height(20.dp))

                    ScreenTitle(
                        if (page == 0) "Первоначальная настройка работы с компанией"
                        else if (
                            viewModel.companyScreenTypeState.value ==
                                CompanyScreenTypesEnum.CREATE_COMPANY
                        )
                            CompanyScreenTypesEnum.CREATE_COMPANY.value
                        else CompanyScreenTypesEnum.JOIN_COMPANY.value
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    when (page) {
                        0 -> {
                            RadioGroup(
                                state = viewModel.companyScreenTypeState,
                                source = CompanyScreenTypesEnum.entries,
                                view = { it.value },
                                comparableKey = { it },
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            InfoCard(
                                icon = Icon.ResourceIcon(R.drawable.i_info),
                                title = "Важно",
                                text =
                                    "Перед началом работы с разделом \"Компания\" необходимо создать свою компанию, либо вступить в уже существующую.",
                            )
                        }

                        1 -> {
                            when (viewModel.companyScreenTypeState.value) {
                                CompanyScreenTypesEnum.CREATE_COMPANY -> {
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
                                }

                                CompanyScreenTypesEnum.JOIN_COMPANY -> {
                                    DropDownTextField(
                                        viewModel.companyListState,
                                        label = "Компании",
                                    )
                                    SinglenessOutlinedTextField(
                                        viewModel.companyLeaderState,
                                        label = "Руководитель",
                                    )
                                    SinglenessOutlinedTextField(
                                        viewModel.companyAddressState,
                                        label = "Адрес",
                                    )
                                }
                            }

                            BackHandler {
                                composableScope.launch { pagerState.animateScrollToPage(0) }
                            }
                        }
                    }

                    BottomContainer(padding = PaddingValues(top = 20.dp, bottom = 10.dp)) {
                        if (page == 1) {
                            SimpleButton(text = "Назад", colors = ButtonDefaults.inverseColors()) {
                                composableScope.launch { pagerState.animateScrollToPage(0) }
                            }

                            Spacer(modifier = Modifier.height(15.dp))
                        }

                        SimpleButton(text = if (page == 0) "Продолжить" else "Присоединиться") {
                            if (page == 0) {
                                composableScope.launch { pagerState.animateScrollToPage(1) }
                            } else {
                                // TODO создание компании или вход в компанию
                            }
                        }
                    }
                }
            }
        } else {
            CompanyScreenMainPage()
        }
    }
}

@Composable
fun CompanyScreenMainPage() {
    Text("Это экран компании")
}

enum class CompanyScreenTypesEnum(val value: String) {
    CREATE_COMPANY(value = "Создать компанию"),
    JOIN_COMPANY(value = "Вступить в компанию"),
}
