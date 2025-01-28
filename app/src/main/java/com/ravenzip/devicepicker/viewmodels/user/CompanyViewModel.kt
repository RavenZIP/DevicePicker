package com.ravenzip.devicepicker.viewmodels.user

import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(private val sharedRepository: SharedRepository) :
    ViewModel() {}
