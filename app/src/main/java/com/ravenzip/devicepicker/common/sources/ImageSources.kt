package com.ravenzip.devicepicker.common.sources

import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageSources @Inject constructor() {
    private val storageRef = Firebase.storage

    /** Корневая директория */
    fun imageSource(): StorageReference {
        return storageRef.reference
    }

    /** Путь до файла конкретного устройства */
    fun imageSourceByPath(brand: String, model: String): StorageReference {
        return storageRef.getReference(brand).child(model)
    }
}
