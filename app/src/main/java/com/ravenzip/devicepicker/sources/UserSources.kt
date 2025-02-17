package com.ravenzip.devicepicker.sources

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSources @Inject constructor() {
    private val usersReference = FirebaseDatabase.getInstance().getReference("Users")

    val root
        get(): DatabaseReference = usersReference

    fun currentUser(userUid: String): DatabaseReference {
        return usersReference.child(userUid)
    }

    fun deviceHistory(userUid: String): DatabaseReference {
        return usersReference.child(userUid).child("deviceHistory")
    }

    fun favourites(userUid: String): DatabaseReference {
        return usersReference.child(userUid).child("favourites")
    }

    fun compares(userUid: String): DatabaseReference {
        return usersReference.child(userUid).child("compares")
    }

    fun companyUid(userUid: String): DatabaseReference {
        return usersReference.child(userUid).child("companyUid")
    }
}
