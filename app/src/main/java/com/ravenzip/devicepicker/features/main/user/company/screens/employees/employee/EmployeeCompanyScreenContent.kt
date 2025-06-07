package com.ravenzip.devicepicker.features.main.user.company.screens.employees.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.components.RowDeviceCard
import com.ravenzip.devicepicker.common.components.SmallText
import com.ravenzip.devicepicker.common.dummy.XIAOMI_PAD_7
import com.ravenzip.devicepicker.common.model.device.compact.DeviceCompact.Companion.convertToDeviceCompactExtended
import com.ravenzip.devicepicker.common.utils.extension.veryLightPrimary
import com.ravenzip.devicepicker.features.main.user.company.screens.info.CompanyInfoViewModel
import com.ravenzip.workshop.components.Icon
import com.ravenzip.workshop.data.icon.IconConfig
import com.ravenzip.workshop.data.icon.IconData

@Composable
fun EmployeeCompanyScreenContent(viewModel: CompanyInfoViewModel, padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            Icon(
                icon = IconData.ResourceIcon(R.drawable.i_user),
                iconConfig = IconConfig(size = 100, color = MaterialTheme.colorScheme.primary),
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        item { UserBaseInfoCard() }

        item { UserDevicesCard() }
    }
}

@Composable
private fun UserBaseInfoCard() {
    Card(
        Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Основные сведения о сотруднике",
                    fontWeight = FontWeight.W500,
                    fontSize = 20.sp,
                    letterSpacing = 0.sp,
                )

                Column {
                    SmallText(text = "Фамилия", fontWeight = FontWeight.W500, letterSpacing = 0.sp)
                    SmallText(text = "Сотников", letterSpacing = 0.sp)
                }

                Column {
                    SmallText(text = "Имя", fontWeight = FontWeight.W500, letterSpacing = 0.sp)
                    SmallText(text = "Андрей", letterSpacing = 0.sp)
                }
                Column {
                    SmallText(text = "Отчество", fontWeight = FontWeight.W500, letterSpacing = 0.sp)
                    SmallText(text = "Игоревич", letterSpacing = 0.sp)
                }
            }
        }
    }
}

@Composable
private fun UserDevicesCard() {
    val x = remember { XIAOMI_PAD_7.convertToDeviceCompactExtended() }

    Card(
        Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.veryLightPrimary(),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Устройства сотрудника",
                    fontWeight = FontWeight.W500,
                    fontSize = 20.sp,
                    letterSpacing = 0.sp,
                )

                RowDeviceCard(modifier = Modifier.fillMaxWidth(), device = x)
            }
        }
    }
}
