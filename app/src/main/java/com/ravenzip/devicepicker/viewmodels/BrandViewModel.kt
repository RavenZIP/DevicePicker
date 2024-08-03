package com.ravenzip.devicepicker.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ravenzip.devicepicker.repositories.BrandRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

@HiltViewModel
class BrandViewModel @Inject constructor(private val brandRepository: BrandRepository) :
    ViewModel() {
    private val _listOfBrand = MutableStateFlow(mutableStateListOf<String>())

    val listOfBrand = _listOfBrand.asStateFlow()

    suspend fun getBrandList(): Flow<Boolean> =
        flow {
                val brandList = brandRepository.getDeviceBrandList()
                _listOfBrand.value.addAll(brandList)
                emit(true)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("BrandViewModel", "${it.message}") }
                emit(false)
            }
}
