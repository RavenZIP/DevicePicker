package com.ravenzip.devicepicker.features.main.user.company.screens.join

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.utils.extension.veryLightPrimary
import com.ravenzip.workshop.components.DropDownTextField
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun CompanyScreenJoinContent(viewModel: JoinToCompanyViewModel) {
    DropDownTextField(
        control = viewModel.form.controls.company,
        state = viewModel.companiesControlState,
        label = "Компания",
    )
    SinglenessOutlinedTextField(control = viewModel.form.controls.leader, label = "Руководитель")
    SinglenessOutlinedTextField(control = viewModel.form.controls.address, label = "Адрес")
    SinglenessOutlinedTextField(control = viewModel.form.controls.code, label = "Код доступа")

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
