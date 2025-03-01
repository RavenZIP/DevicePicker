package com.ravenzip.devicepicker.ui.screens.main.user.company.join

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.extensions.functions.inverseColors
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.JoinToCompanyViewModel
import com.ravenzip.workshop.components.DropDownTextField
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.data.icon.IconData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyScreenJoinContent(viewModel: JoinToCompanyViewModel) {
    val composableScope = rememberCoroutineScope()

    DropDownTextField(viewModel.companyState, label = "Компания")
    SinglenessOutlinedTextField(viewModel.companyLeaderState, label = "Руководитель")
    SinglenessOutlinedTextField(viewModel.companyAddressState, label = "Адрес")
    SinglenessOutlinedTextField(viewModel.companyCodeState, label = "Код доступа")

    Spacer(modifier = Modifier.height(5.dp))

    InfoCard(
        icon = IconData.ResourceIcon(R.drawable.i_info),
        title = "Важно",
        text =
            "Для присоединения к уже существующей компании выберите компанию из списка, " +
                "введите код доступа и нажмите на кнопку \"Присоединиться\"",
        colors = CardDefaults.veryLightPrimary(),
    )

    BottomContainer(padding = PaddingValues(top = 20.dp, bottom = 10.dp)) {
        SimpleButton(text = "Назад", colors = ButtonDefaults.inverseColors()) {
            composableScope.launch { viewModel.navigateTo.emit(CompanyGraph.COMPANY_ROOT) }
        }

        Spacer(modifier = Modifier.height(15.dp))

        SimpleButton(text = "Присоединиться") {
            composableScope.launch { viewModel.joinToCompany.emit(Unit) }
        }
    }
}
