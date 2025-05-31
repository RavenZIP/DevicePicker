package com.ravenzip.devicepicker.features.main.compare

import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.common.enums.DeviceTypeEnum
import com.ravenzip.devicepicker.common.enums.TagsEnum
import com.ravenzip.devicepicker.common.model.device.Device
import com.ravenzip.devicepicker.common.model.device.compact.DeviceCompact
import com.ravenzip.devicepicker.common.model.device.compact.DeviceSpecifications
import com.ravenzip.devicepicker.common.model.device.configurations.PhoneConfigurationDto
import com.ravenzip.devicepicker.common.model.device.specifications.BaseInfoDto
import com.ravenzip.workshop.forms.control.FormControl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompareScreenViewModel @Inject constructor() : ViewModel() {
    val selectedListControl = FormControl(initialValue = DeviceTypeEnum.SMARTPHONE)

    val firstDeviceCompact =
        DeviceCompact(
            uid = "-OR7s8NHnbxsPp33LVxk",
            type = "Смартфон",
            model = "Samsung Galaxy A25",
            diagonal = 0.0,
            cpu = "",
            battery = 0,
            camera = 0,
            rating = 4.76,
            reviewsCount = 492,
            brand = "Samsung",
            tags = listOf(TagsEnum.POPULAR, TagsEnum.USER_SELECTION, TagsEnum.LIGHT_WEIGHT),
            imageUrl =
                "https://firebasestorage.googleapis.com/v0/b/devicepicker-d290d.appspot.com/o/Samsung%2FSamsung%20Galaxy%20A25%2FSamsung%20Galaxy%20A25.webp?alt=media&token=7babc691-8a2e-4624-ac3a-ebb2a0d5b71d",
        )

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

    val secondDeviceCompact =
        DeviceCompact(
            uid = "-O-X4qNXc2Tp7jrWc5-I",
            type = "Смартфон",
            model = "Redmi Note 7",
            diagonal = 0.0,
            cpu = "",
            battery = 0,
            camera = 0,
            rating = 4.89,
            reviewsCount = 300,
            brand = "Redmi",
            tags = listOf(TagsEnum.OLD_MODEL),
            imageUrl =
                "https://firebasestorage.googleapis.com/v0/b/devicepicker-d290d.appspot.com/o/Redmi%2FRedmi%20Note%207%2FRedmi%20Note%207.webp?alt=media&token=c7488f90-a10a-4ffd-b612-dd10efb18449",
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
