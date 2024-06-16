package com.ravenzip.devicepicker.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceCompactSources @Inject constructor() {
    private val deviceCompactRef = FirebaseDatabase.getInstance().getReference("DeviceCompact")

    fun deviceCompactSource(): DatabaseReference {
        return deviceCompactRef
    }
}
