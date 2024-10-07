package com.ravenzip.devicepicker.state

/** Состояние UI */
sealed class UiState<out T> {
    /** Дефолтное состояние */
    class Default : UiState<Nothing>()

    /** Загрузка данных */
    data class Loading(val message: String) : UiState<Nothing>()

    /** Успех. Данные загружены */
    data class Success<T>(val data: T) : UiState<T>()

    /** Работа с диалоговым окном */
    sealed class Dialog : UiState<Nothing>() {
        /** Отображено на экране */
        class Opened() : Dialog()

        /** Нажата кнопка подтверждения */
        class Confirmed() : Dialog()
    }

    /** Ошибка при обработке данных */
    data class Error(val message: String) : UiState<Nothing>()
}
