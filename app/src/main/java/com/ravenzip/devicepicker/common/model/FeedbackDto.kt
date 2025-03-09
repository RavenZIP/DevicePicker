package com.ravenzip.devicepicker.common.model

/**
 * [FeedbackDto]
 *
 * Описывает рейтинг и количество отзывов и вопросов
 */
data class FeedbackDto(val rating: Double, val reviewsCount: Int, val questionsCount: Int) {
    constructor() : this(rating = 0.0, reviewsCount = 0, questionsCount = 0)
}
