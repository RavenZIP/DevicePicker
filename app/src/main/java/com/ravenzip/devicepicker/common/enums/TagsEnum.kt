package com.ravenzip.devicepicker.common.enums

import androidx.compose.ui.graphics.Color
import com.ravenzip.devicepicker.R
import com.ravenzip.devicepicker.common.enums.TagsEnum.HEAVY
import com.ravenzip.devicepicker.common.enums.TagsEnum.HIGH_PERFORMANCE
import com.ravenzip.devicepicker.common.enums.TagsEnum.LIGHT_WEIGHT
import com.ravenzip.devicepicker.common.enums.TagsEnum.LOW_QUALITY
import com.ravenzip.devicepicker.common.enums.TagsEnum.NEW_MODEL
import com.ravenzip.devicepicker.common.enums.TagsEnum.OLD_MODEL
import com.ravenzip.devicepicker.common.enums.TagsEnum.POPULAR
import com.ravenzip.devicepicker.common.enums.TagsEnum.RELIABLE
import com.ravenzip.devicepicker.common.enums.TagsEnum.SECURITY
import com.ravenzip.devicepicker.common.enums.TagsEnum.USER_SELECTION
import com.ravenzip.devicepicker.common.theme.heavyTagColor
import com.ravenzip.devicepicker.common.theme.highPerformanceTagColor
import com.ravenzip.devicepicker.common.theme.lightWeightTagColor
import com.ravenzip.devicepicker.common.theme.lowQualityColor
import com.ravenzip.devicepicker.common.theme.newModelColor
import com.ravenzip.devicepicker.common.theme.oldModelTagColor
import com.ravenzip.devicepicker.common.theme.popularTagColor
import com.ravenzip.devicepicker.common.theme.reliableColor
import com.ravenzip.devicepicker.common.theme.securityTagColor
import com.ravenzip.devicepicker.common.theme.userSelectionTagColor

/**
 * Метки устройств
 *
 * [HIGH_PERFORMANCE] - Производительный
 *
 * [POPULAR] - Популярный
 *
 * [RELIABLE] - Надежный
 *
 * [OLD_MODEL] - Тяжелый
 *
 * [NEW_MODEL] - Новая модель
 *
 * [LIGHT_WEIGHT] - Легкий
 *
 * [HEAVY] - Тяжелый
 *
 * [SECURITY] - Защищенный
 *
 * [LOW_QUALITY] - Низкое качество
 *
 * [USER_SELECTION] - Выбор пользователей
 */
enum class TagsEnum(val value: String, val description: String, val color: Color, val icon: Int) {
    POPULAR(
        value = "Популярный",
        description =
            "Данное устройство считается популярным, поскольку количество просмотров, " +
                "отзывов и вопросов за последний месяц больше среднего значения по всем устройствам",
        color = popularTagColor,
        icon = R.drawable.i_flame,
    ),
    NEW_MODEL(
        value = "Новинка",
        description = "Данная модель считается новой, поскольку ее выпуск состоялся в текущем году",
        color = newModelColor,
        icon = R.drawable.i_new,
    ),
    HIGH_PERFORMANCE(
        value = "Производительный",
        description =
            "Данное устройство считается производительным, поскольку его характеристики лучше, " +
                "чем у большинства других устройств",
        color = highPerformanceTagColor,
        icon = R.drawable.i_speedometer,
    ),
    USER_SELECTION(
        value = "Выбор пользователей",
        description = "Описание...",
        color = userSelectionTagColor,
        icon = R.drawable.i_medal,
    ),
    RELIABLE(
        value = "Надежный",
        description = "Описание...",
        color = reliableColor,
        icon = R.drawable.i_trust,
    ),
    LIGHT_WEIGHT(
        value = "Легкий",
        description = "У данной модели легкий вес",
        color = lightWeightTagColor,
        icon = R.drawable.i_light_weight,
    ),
    SECURITY(
        value = "Защищенный",
        description = "Данная модель считается защищенной, поскольку...",
        color = securityTagColor,
        icon = R.drawable.i_security,
    ),
    OLD_MODEL(
        value = "Устаревший",
        description = "Данная модель считается устаревшей, поскольку вышла 4 или более лет назад",
        color = oldModelTagColor,
        icon = R.drawable.i_history,
    ),
    HEAVY(
        value = "Тяжелый",
        description = "У данной модели тяжелый вес",
        color = heavyTagColor,
        icon = R.drawable.i_heavy,
    ),
    LOW_QUALITY(
        value = "Низкое качество",
        description = "Описание...",
        color = lowQualityColor,
        icon = R.drawable.i_problem_report,
    ),
}
