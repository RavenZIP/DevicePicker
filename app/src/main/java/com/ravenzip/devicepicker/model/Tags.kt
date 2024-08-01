package com.ravenzip.devicepicker.model

import com.ravenzip.devicepicker.model.device.Tag
import com.ravenzip.devicepicker.model.device.Tag.Companion.createBooleanTag
import com.ravenzip.devicepicker.model.device.Tag.Companion.createDoubleWithBooleanTag

/**
 * Метки устройств
 *
 * [popular] - Популярный
 *
 * [lowPrice] - Низкая цена
 *
 * [highPerformance] - Производительный
 *
 * [energyEfficient] - Энергоэффективный
 *
 * [reliable] - Надежный
 *
 * [newModel] - Новая модель
 *
 * [highQualityScreen] - Качественный экран
 *
 * [highQualityConnection] - Качественная связь
 *
 * [highQualityCamera] - Качественные камеры
 *
 * [overallRating] - Общий рейтинг
 *
 * [priceSegment] - Ценовой сегмент
 */
class Tags(
    val popular: Tag<Double, Boolean>,
    val lowPrice: Tag<Boolean, Boolean>,
    val highPerformance: Tag<Double, Boolean>,
    val energyEfficient: Tag<Double, Boolean>,
    val reliable: Tag<Boolean, Boolean>,
    val newModel: Tag<Boolean, Boolean>,
    val highQualityScreen: Tag<Double, Boolean>,
    val highQualityConnection: Tag<Boolean, Boolean>,
    val highQualityCamera: Tag<Boolean, Boolean>,
    val overallRating: Tag<Boolean, Boolean>,
    val priceSegment: Tag<Boolean, Boolean>
) {
    constructor() :
        this(
            popular = createDoubleWithBooleanTag(),
            lowPrice = createBooleanTag(),
            highPerformance = createDoubleWithBooleanTag(),
            energyEfficient = createDoubleWithBooleanTag(),
            reliable = createBooleanTag(),
            newModel = createBooleanTag(),
            highQualityScreen = createDoubleWithBooleanTag(),
            highQualityConnection = createBooleanTag(),
            highQualityCamera = createBooleanTag(),
            overallRating = createBooleanTag(),
            priceSegment = createBooleanTag())
}
