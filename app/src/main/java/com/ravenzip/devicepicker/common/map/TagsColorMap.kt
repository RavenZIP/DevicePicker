package com.ravenzip.devicepicker.common.map

import com.ravenzip.devicepicker.common.enums.TagsEnum
import com.ravenzip.devicepicker.common.theme.energyEfficientTagColor
import com.ravenzip.devicepicker.common.theme.highPerformanceTagColor
import com.ravenzip.devicepicker.common.theme.highQualityScreenColor
import com.ravenzip.devicepicker.common.theme.lowPriceTagColor
import com.ravenzip.devicepicker.common.theme.newModelColor
import com.ravenzip.devicepicker.common.theme.popularTagColor
import com.ravenzip.devicepicker.common.theme.reliableColor

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
