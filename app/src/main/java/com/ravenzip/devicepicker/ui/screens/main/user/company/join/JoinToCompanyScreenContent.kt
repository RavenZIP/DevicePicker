package com.ravenzip.devicepicker.ui.screens.main.user.company.join

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.extensions.functions.veryLightPrimary
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.JoinToCompanyViewModel
import com.ravenzip.workshop.components.DropDownTextField
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.data.icon.IconData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyScreenJoinContent(viewModel: JoinToCompanyViewModel) {
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

    Spacer(modifier = Modifier.height(10.dp))
}
