package com.ravenzip.devicepicker.constants.map

import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.ui.theme.energyEfficientTagColor
import com.ravenzip.devicepicker.ui.theme.highPerformanceTagColor
import com.ravenzip.devicepicker.ui.theme.highQualityScreenColor
import com.ravenzip.devicepicker.ui.theme.lowPriceTagColor
import com.ravenzip.devicepicker.ui.theme.newModelColor
import com.ravenzip.devicepicker.ui.theme.popularTagColor
import com.ravenzip.devicepicker.ui.theme.reliableColor

val tagsColorMap =
    mapOf(
        TagsEnum.POPULAR to popularTagColor,
        TagsEnum.HIGH_PERFORMANCE to highPerformanceTagColor,
        TagsEnum.LOW_PRICE to lowPriceTagColor,
        TagsEnum.ENERGY_EFFICIENT to energyEfficientTagColor,
        TagsEnum.RELIABLE to reliableColor,
        TagsEnum.NEW_MODEL to newModelColor,
        TagsEnum.HIGH_QUALITY_SCREEN to highQualityScreenColor,
    )
