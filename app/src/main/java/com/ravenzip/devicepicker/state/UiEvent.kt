package com.ravenzip.devicepicker.state

/** Действия UI */
sealed class UiEvent {
    /** Выполнить навигацию */
    class Navigate() : UiEvent()

    /** Отобразить снэкбар */
    sealed class ShowSnackBar() : UiEvent() {
        class Default(val message: String) : ShowSnackBar()

        class Success(val message: String) : ShowSnackBar()

        class Warning(val message: String) : ShowSnackBar()

        class Error(val message: String) : ShowSnackBar()
    }
}
