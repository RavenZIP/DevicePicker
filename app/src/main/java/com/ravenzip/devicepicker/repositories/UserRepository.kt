package com.ravenzip.devicepicker.repositories

import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.sources.UserSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class UserRepository @Inject constructor(private val userSources: UserSources) {
    suspend fun getUser(userUid: String): User {
        val response = userSources.userSourceByUid(userUid).get().await()
        val res = response.getValue<User>()
        return res ?: User()
    }

    suspend fun addUser(userUid: String) {
        userSources.userSourceByUid(userUid).setValue(User()).await()
    }
}
