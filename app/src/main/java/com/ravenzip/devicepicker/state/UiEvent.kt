package com.ravenzip.devicepicker.state

/** Действия UI */
sealed class UiEvent {
    /** Выполнить навигацию */
    // TODO временно дефолтная инициализация как пустая строка, потом убрать
    data class Navigate(val route: String = "") : UiEvent()

    /** Отобразить снэкбар */
    sealed class ShowSnackBar() : UiEvent() {
        class Default(val message: String) : ShowSnackBar()

        class Success(val message: String) : ShowSnackBar()

        class Warning(val message: String) : ShowSnackBar()

        class Error(val message: String) : ShowSnackBar()
    }
}
