package com.ravenzip.devicepicker.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceSources @Inject constructor() {
    private val deviceRef = FirebaseDatabase.getInstance().getReference("Device")
    private val deviceCompactRef = FirebaseDatabase.getInstance().getReference("DeviceCompact")

    fun deviceSource(): DatabaseReference {
        return deviceRef
    }

    fun deviceSourceByUid(uid: String): DatabaseReference {
        return deviceRef.child(uid)
    }

    fun deviceCompactSource(): DatabaseReference {
        return deviceCompactRef
    }
}
