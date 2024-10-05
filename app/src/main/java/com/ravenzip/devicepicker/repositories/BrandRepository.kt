package com.ravenzip.devicepicker.repositories

import android.util.Log
import com.ravenzip.devicepicker.extensions.functions.convertToClass
import com.ravenzip.devicepicker.sources.BrandSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class BrandRepository @Inject constructor(private val brandSources: BrandSources) {
    fun getBrandList() =
        flow {
                val response = brandSources.brandSource().get().await().children
                val brandList = response.convertToClass<String>()
                emit(brandList)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getBrandList", "${it.message}") }
                emit(listOf())
            }
}
