package com.ravenzip.devicepicker.common.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceTypeSources @Inject constructor() {
    private val deviceTypeRef = FirebaseDatabase.getInstance().getReference("DeviceType")

    fun deviceTypeSource(): DatabaseReference {
        return deviceTypeRef
    }
}
