package com.ravenzip.devicepicker.common.model

/** Действия UI */
sealed class UiEvent {
    /** Выполнить навигацию */
    sealed class Navigate : UiEvent() {
        data object Next : Navigate()

        data class ByRoute(val route: String) : Navigate()

        data class WithoutBackStack(val route: String) : Navigate()

        data object Back : Navigate()

        data object Parent : Navigate()
    }

    /** Отобразить снэкбар */
    sealed class ShowSnackBar : UiEvent() {
        data class Default(val message: String) : ShowSnackBar()

        data class Success(val message: String) : ShowSnackBar()

        data class Warning(val message: String) : ShowSnackBar()

        data class Error(val message: String) : ShowSnackBar()
    }
}
