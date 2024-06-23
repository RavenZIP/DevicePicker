package com.ravenzip.devicepicker.repositories

import com.ravenzip.devicepicker.extensions.functions.convertToClass
import com.ravenzip.devicepicker.sources.BrandSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class BrandRepository @Inject constructor(private val brandSources: BrandSources) {
    suspend fun getDeviceBrandList(): List<String> {
        val response = brandSources.brandSource().get().await().children
        return response.convertToClass<String>()
    }
}
