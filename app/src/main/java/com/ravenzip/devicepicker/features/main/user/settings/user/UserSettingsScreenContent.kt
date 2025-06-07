package com.ravenzip.devicepicker.features.main.user.settings.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.workshop.components.SinglenessOutlinedTextField

@Composable
fun UserSettingsScreenContent(viewModel: UserSettingsScreenViewModel, padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            Text(
                text = "Основные сведения о пользователе",
                modifier = Modifier.fillMaxSize(0.9f),
                fontSize = 18.sp,
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                SinglenessOutlinedTextField(
                    control = viewModel.form.controls.surname,
                    label = "Фамилия",
                )
                SinglenessOutlinedTextField(control = viewModel.form.controls.name, label = "Имя")
                SinglenessOutlinedTextField(
                    control = viewModel.form.controls.patronymic,
                    label = "Отчество",
                )
            }
        }

        item {
            Text(
                text = "Сведения о регистрации",
                modifier = Modifier.fillMaxSize(0.9f),
                fontSize = 18.sp,
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                SinglenessOutlinedTextField(
                    control = viewModel.form.controls.email,
                    label = "Электронная почта",
                )
            }
        }
    }
}
