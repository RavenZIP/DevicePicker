package com.ravenzip.devicepicker.common.model

/** Состояния диалогового окна */
sealed class DialogState {
    /** Отображается */
    class Showed : DialogState()

    /** Отклонено */
    class Dismissed : DialogState()

    /** Подтверждено */
    class Confirmed : DialogState()
}
