package com.ravenzip.devicepicker.model

/**
 * [Feedback]
 *
 * Описывает рейтинг и количество отзывов и вопросов
 */
data class Feedback(val rating: Double, val reviewsCount: Int, val questionsCount: Int) {
    constructor() : this(rating = 0.0, reviewsCount = 0, questionsCount = 0)
}
