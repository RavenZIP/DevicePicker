package com.ravenzip.devicepicker.ui.screens.main.user.company.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.devicepicker.repositories.SharedRepository
import com.ravenzip.devicepicker.state.UiEvent
import com.ravenzip.devicepicker.ui.screens.main.user.company.enum.CompanyScreenActionsEnum
import com.ravenzip.workshop.forms.state.FormState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class CompanyRootViewModel @Inject constructor(sharedRepository: SharedRepository) : ViewModel() {
    val companyScreenTypeState = FormState(CompanyScreenActionsEnum.CREATE_COMPANY)

    val navigateByCompanyScreenType = MutableSharedFlow<Unit>()
    val navigateBackToParent = MutableSharedFlow<Unit>()

    private val _companyUid =
        sharedRepository.userData
            .map { userData -> userData.companyUid }
            .stateIn(scope = viewModelScope, started = SharingStarted.Lazily, initialValue = "")

    private val companyUidChangedToNotEmpty = _companyUid.filter { it.isNotEmpty() }

    val uiEvent =
        merge(
            navigateByCompanyScreenType.map {
                val route =
                    if (companyScreenTypeState.value == CompanyScreenActionsEnum.CREATE_COMPANY)
                        CompanyGraph.CREATE_COMPANY
                    else CompanyGraph.JOIN_TO_COMPANY

                UiEvent.Navigate.ByRoute(route)
            },
            companyUidChangedToNotEmpty.map { uid ->
                UiEvent.Navigate.WithoutBackStack("${CompanyGraph.COMPANY_INFO}/${uid}")
            },
            navigateBackToParent.map { UiEvent.Navigate.Parent },
        )
}
