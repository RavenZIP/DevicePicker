package com.ravenzip.devicepicker.common.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BrandSources @Inject constructor() {
    private val brandRef = FirebaseDatabase.getInstance().getReference("Brand")

    fun brandSource(): DatabaseReference {
        return brandRef
    }
}
