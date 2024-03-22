package com.ravenzip.devicepicker.services

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.storage.storage
import com.ravenzip.devicepicker.data.device.FirebaseImageData
import com.ravenzip.devicepicker.data.device.compact.DeviceCompact
import com.ravenzip.devicepicker.data.device.compact.FirebaseDeviceCompact
import com.ravenzip.devicepicker.extensions.functions.convertToImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DataService : ViewModel() {
    private val databaseInstance = FirebaseDatabase.getInstance()
    private val storage = Firebase.storage

    private val _devices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _popularThisWeek = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _similarDevices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _companyBestDevices = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _lowPrice = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _unknown = MutableStateFlow(mutableListOf<DeviceCompact>())
    private val _isLoading = MutableStateFlow(false)

    val devices = _devices.asStateFlow()
    val popularThisWeek = _popularThisWeek.asStateFlow()
    val similarDevices = _similarDevices.asStateFlow()
    val companyBestDevices = _companyBestDevices.asStateFlow()
    val lowPrice = _lowPrice.asStateFlow()
    val unknown = _unknown.asStateFlow()
    val isLoading = _isLoading.asStateFlow()

    /** Получить список популярных за неделю устройств */
    suspend fun getPopularThisWeek() {
        try {
            val startTime = System.currentTimeMillis()
            val popularThisWeekRef =
                databaseInstance.getReference("PromotionsNew").child("Popular this week")
            val popularThisWeek = popularThisWeekRef.get().await().children

            popularThisWeek.forEach { data ->
                val item = data.getValue<FirebaseDeviceCompact>()
                if (item !== null) {
                    // TODO: получать изображения отдельным запросом параллельно
                    val image = getImage(item.model, item.imageData)
                    val device = item.convertToDeviceCompact(image)
                    _popularThisWeek.value.add(device)
                }
            }
            Log.d(
                "getPopularThisWeek time s",
                ((System.currentTimeMillis() - startTime).toDouble() / 1000).toString()
            )
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("getPromotions", "${e.message}") }
            _popularThisWeek.value.add(DeviceCompact())
        }
    }

    /** Получить список устройств на основе недавно просмотренных */
    suspend fun getSimilarDevices() {
        try {
            val startTime = System.currentTimeMillis()
            val similarDevicesRef =
                databaseInstance.getReference("PromotionsNew").child("Similar devices")
            val similarDevices = similarDevicesRef.get().await().children

            similarDevices.forEach { data ->
                val item = data.getValue<FirebaseDeviceCompact>()
                if (item !== null) {
                    val image = getImage(item.model, item.imageData)
                    val device = item.convertToDeviceCompact(image)
                    _similarDevices.value.add(device)
                }
            }
            Log.d(
                "getSimilarDevices time s",
                ((System.currentTimeMillis() - startTime).toDouble() / 1000).toString()
            )
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("getPromotions", "${e.message}") }
            _similarDevices.value.add(DeviceCompact())
        }
    }

    /** Получить список лучших устройств компании */
    suspend fun getCompanyBestDevices() {
        try {
            val startTime = System.currentTimeMillis()
            val companyBestDevicesRef =
                databaseInstance.getReference("PromotionsNew").child("Redmi: The best devices")
            val companyBestDevices = companyBestDevicesRef.get().await().children

            companyBestDevices.forEach { data ->
                val item = data.getValue<FirebaseDeviceCompact>()
                if (item !== null) {
                    val image = getImage(item.model, item.imageData)
                    val device = item.convertToDeviceCompact(image)
                    _companyBestDevices.value.add(device)
                }
            }
            Log.d(
                "getCompanyBestDevices time s",
                ((System.currentTimeMillis() - startTime).toDouble() / 1000).toString()
            )
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("getPromotions", "${e.message}") }
            _companyBestDevices.value.add(DeviceCompact())
        }
    }

    /** Получить список устройств с низкой ценой */
    suspend fun getLowPrice() {
        try {
            val startTime = System.currentTimeMillis()
            val lowPriceRef = databaseInstance.getReference("PromotionsNew").child("Low price")
            val lowPrice = lowPriceRef.get().await().children

            lowPrice.forEach { data ->
                val item = data.getValue<FirebaseDeviceCompact>()
                if (item !== null) {
                    val image = getImage(item.model, item.imageData)
                    val device = item.convertToDeviceCompact(image)
                    _lowPrice.value.add(device)
                }
            }
            Log.d(
                "getLowPrice time s",
                ((System.currentTimeMillis() - startTime).toDouble() / 1000).toString()
            )
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("getPromotions", "${e.message}") }
            _lowPrice.value.add(DeviceCompact())
        }
    }

    /** Получить список устройств... */
    suspend fun getUnknown() {
        try {
            val startTime = System.currentTimeMillis()
            val unknownRef = databaseInstance.getReference("PromotionsNew").child("Title")
            val unknown = unknownRef.get().await().children

            unknown.forEach { data ->
                val item = data.getValue<FirebaseDeviceCompact>()
                if (item !== null) {
                    val image = getImage(item.model, item.imageData)
                    val device = item.convertToDeviceCompact(image)
                    _unknown.value.add(device)
                }
            }
            Log.d(
                "getUnknown time s",
                ((System.currentTimeMillis() - startTime).toDouble() / 1000).toString()
            )
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("getPromotions", "${e.message}") }
            _unknown.value.add(DeviceCompact())
        }
    }

    /** Получить основное изображение конкретного товара */
    private suspend fun getImage(fileName: String, imageData: FirebaseImageData): ImageBitmap {
        return try {
            val image =
                storage
                    .getReference(fileName + imageData.extension)
                    .getBytes(imageData.size)
                    .await()
            image.convertToImageBitmap()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.d("getImage", "${e.message}") }
            ImageBitmap(150, 150)
        }
    }

    /** Получить список устройств */
    suspend fun getDevices() {
        val database = databaseInstance.getReference("Devices")
        val itemsList = database.child("Redmi").get().await().children

        itemsList.forEach {
            val item = it.getValue<FirebaseDeviceCompact>()
            if (item !== null) {
                val image = getImage(item.model, item.imageData)
                val device = item.convertToDeviceCompact(image)
                _devices.value.add(device)
            }
        }
    }

    /** Получить все изображения из корневой директории */
    suspend fun getAllImages() {
        val test1 = storage.reference.listAll().await()
    }

    /** Получить все изображения из указанной директории */
    suspend fun getAllImages(reference: String) {
        val test1 = storage.getReference(reference).listAll().await()
    }
}
