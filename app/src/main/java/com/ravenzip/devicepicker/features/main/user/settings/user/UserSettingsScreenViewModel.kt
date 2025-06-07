package com.ravenzip.devicepicker.features.main.user.settings.user

import androidx.lifecycle.ViewModel
import com.ravenzip.workshop.forms.control.FormControl
import com.ravenzip.workshop.forms.group.FormGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserSettingsScreenViewModel @Inject constructor() : ViewModel() {
    val form =
        FormGroup(
            UserProfileForm(
                surname = FormControl("Не указано", disable = true),
                name = FormControl("Не указано", disable = true),
                patronymic = FormControl("Не указано", disable = true),
                email = FormControl("sasha2012773@yandex.ru", disable = true),
            )
        )
}
