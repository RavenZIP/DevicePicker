package com.ravenzip.devicepicker.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSources @Inject constructor() {
    private val userRef = FirebaseDatabase.getInstance().getReference("Users")

    fun userSource(): DatabaseReference {
        return userRef
    }

    fun userSourceByUid(userUid: String): DatabaseReference {
        return userRef.child(userUid)
    }
}
