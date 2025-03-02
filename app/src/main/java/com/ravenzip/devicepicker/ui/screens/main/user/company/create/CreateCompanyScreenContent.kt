package com.ravenzip.devicepicker.ui.screens.main.user.company.create

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
import com.ravenzip.devicepicker.ui.components.BottomContainer
import com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel.CreateCompanyViewModel
import com.ravenzip.workshop.components.InfoCard
import com.ravenzip.workshop.components.MultilineTextField
import com.ravenzip.workshop.components.SimpleButton
import com.ravenzip.workshop.components.SinglenessOutlinedTextField
import com.ravenzip.workshop.data.icon.IconData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyScreenCreateContent(viewModel: CreateCompanyViewModel) {
    val composableScope = rememberCoroutineScope()

    SinglenessOutlinedTextField(viewModel.companyNameState, label = "Наименование")
    MultilineTextField(viewModel.companyDescriptionState, label = "Описание")
    SinglenessOutlinedTextField(viewModel.companyAddressState, label = "Адрес")
    SinglenessOutlinedTextField(viewModel.companyCodeState, label = "Код доступа")

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

    BottomContainer(padding = PaddingValues(top = 20.dp, bottom = 10.dp)) {
        SimpleButton(text = "Назад", colors = ButtonDefaults.inverseColors()) {
            composableScope.launch { viewModel.navigateBack.emit(Unit) }
        }

        Spacer(modifier = Modifier.height(15.dp))

        SimpleButton(text = "Создать") {
            composableScope.launch { viewModel.createCompany.emit(Unit) }
        }
    }
}
