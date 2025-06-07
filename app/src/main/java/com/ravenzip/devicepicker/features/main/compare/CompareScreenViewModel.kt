package com.ravenzip.devicepicker.features.main.compare

import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.common.enums.DeviceTypeEnum
import com.ravenzip.devicepicker.common.model.device.Device
import com.ravenzip.devicepicker.common.model.device.compact.DeviceSpecifications
import com.ravenzip.devicepicker.common.model.device.configurations.PhoneConfigurationDto
import com.ravenzip.devicepicker.common.model.device.specifications.BaseInfoDto
import com.ravenzip.workshop.forms.control.FormControl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompareScreenViewModel @Inject constructor() : ViewModel() {
    val selectedListControl = FormControl(initialValue = DeviceTypeEnum.SMARTPHONE)

    val firstDevice =
        Device(
            configurations =
                listOf(
                    PhoneConfigurationDto(randomAccessMemory = 6, internalMemory = 128),
                    PhoneConfigurationDto(randomAccessMemory = 8, internalMemory = 256),
                ),
            specifications =
                DeviceSpecifications(
                    baseInfo =
                        BaseInfoDto(
                            type = "Смартфон",
                            model = "Samsung Galaxy A25",
                            country = "Вьетнам",
                            year = 2023,
                        )
                ),
        )

    val secondDevice =
        Device(
            configurations =
                listOf(
                    PhoneConfigurationDto(randomAccessMemory = 3, internalMemory = 32),
                    PhoneConfigurationDto(randomAccessMemory = 4, internalMemory = 64),
                    PhoneConfigurationDto(randomAccessMemory = 4, internalMemory = 128),
                ),
            specifications =
                DeviceSpecifications(
                    baseInfo =
                        BaseInfoDto(
                            type = "Смартфон",
                            model = "Redmi Note 7",
                            country = "Китай",
                            year = 2019,
                        )
                ),
        )
}
