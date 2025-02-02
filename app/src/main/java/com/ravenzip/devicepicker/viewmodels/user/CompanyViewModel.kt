package com.ravenzip.devicepicker.viewmodels.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.repositories.CompanyRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import com.ravenzip.devicepicker.ui.screens.main.user.company.CompanyScreenTypesEnum
import com.ravenzip.workshop.forms.state.FormState
import com.ravenzip.workshop.forms.state.special.DropDownTextFieldState
import com.ravenzip.workshop.forms.state.special.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CompanyViewModel
@Inject
constructor(sharedRepository: SharedRepository, private val companyRepository: CompanyRepository) :
    ViewModel() {
    val companyUid =
        sharedRepository.userData
            .map { userData -> userData.companyUid }
            .stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = "")

    val companyScreenTypeState = FormState(CompanyScreenTypesEnum.CREATE_COMPANY)

    val companyNameState = TextFieldState(initialValue = "")
    val companyDescriptionState = TextFieldState(initialValue = "")
    val companyAddressState = TextFieldState(initialValue = "")

    val companyListState =
        DropDownTextFieldState(initialValue = "", items = listOf(), itemsView = { item -> item })
    val companyLeaderState = TextFieldState(initialValue = "", disable = true)

    init {
        viewModelScope.launch {
            companyScreenTypeState.valueChanges.collect { type ->
                companyNameState.reset()
                companyDescriptionState.reset()
                companyAddressState.reset()
                companyListState.reset()
                companyLeaderState.reset()

                if (type == CompanyScreenTypesEnum.CREATE_COMPANY) {
                    companyAddressState.enable()
                } else {
                    companyAddressState.disable()
                }
            }
        }
    }
}
