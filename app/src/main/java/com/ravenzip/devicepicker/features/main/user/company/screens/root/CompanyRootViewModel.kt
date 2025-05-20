package com.ravenzip.devicepicker.features.main.user.company.screens.root

import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.common.model.UiEvent
import com.ravenzip.devicepicker.common.repositories.SharedRepository
import com.ravenzip.devicepicker.features.main.user.company.enum.CompanyScreenActionsEnum
import com.ravenzip.devicepicker.navigation.models.CompanyGraph
import com.ravenzip.workshop.forms.control.FormControl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

@HiltViewModel
class CompanyRootViewModel @Inject constructor(sharedRepository: SharedRepository) : ViewModel() {
    val companyScreenTypeState = FormControl(CompanyScreenActionsEnum.CREATE_COMPANY)

    val navigateByCompanyScreenType = MutableSharedFlow<Unit>()
    val navigateBackToParent = MutableSharedFlow<Unit>()

    private val companyUidChangedToNotEmpty =
        sharedRepository.userData
            .map { userData -> userData.companyUid }
            .filter { companyUid -> companyUid.isNotEmpty() }

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
