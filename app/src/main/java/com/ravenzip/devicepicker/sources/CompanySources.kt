package com.ravenzip.devicepicker.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanySources @Inject constructor() {
    private val companyReference = FirebaseDatabase.getInstance().getReference("Company")

    fun companyBaseSource(): DatabaseReference {
        return companyReference
    }

    fun companyByUid(uid: String): DatabaseReference {
        return companyReference.child(uid)
    }

    fun companyMembers(uid: String): DatabaseReference {
        return companyByUid(uid).child("members")
    }
}
