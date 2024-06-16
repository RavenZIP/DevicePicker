package com.ravenzip.devicepicker.sources

import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageSources @Inject constructor() {
    private val storageRef = Firebase.storage

    fun imageSource(): StorageReference {
        return storageRef.reference
    }

    fun imageSourceByPath(path: String): StorageReference {
        return storageRef.getReference(path)
    }
}
