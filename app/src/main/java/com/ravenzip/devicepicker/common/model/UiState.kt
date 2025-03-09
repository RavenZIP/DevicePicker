package com.ravenzip.devicepicker.common.model

/** Состояние UI */
sealed class UiState<out T> {
    /** Загрузка данных */
    data class Loading(val message: String) : UiState<Nothing>()

    /** Успех. Данные загружены */
    data class Success<T>(val data: T) : UiState<T>()

    /** Ошибка при обработке данных */
    data class Error(val message: String) : UiState<Nothing>()
}
