package com.ravenzip.devicepicker.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.ravenzip.devicepicker.constants.enums.TagsEnum
import com.ravenzip.devicepicker.constants.map.tagIconMap
import com.ravenzip.devicepicker.constants.map.tagsColorMap
import com.ravenzip.workshop.data.IconParameters

/**
 * Метки устройств
 *
 * [computedTags] - Вычисленные метки
 *
 * [manualTags] - Метки, проставленные вручную
 */
class Tags(val computedTags: List<TagsEnum>, val manualTags: List<TagsEnum>) {
    constructor() : this(computedTags = listOf(), manualTags = listOf())

    fun Tags.createListOfUniqueTags() = listOf(computedTags, manualTags).flatten().distinct()

    companion object {
        @Composable
        fun Tags.createListOfChipIcons() =
            this.createListOfUniqueTags().map { tag ->
                IconParameters(
                    value = ImageVector.vectorResource(tagIconMap[tag]!!),
                    size = 20,
                    color = tagsColorMap[tag])
            }
    }
}
