package com.ravenzip.devicepicker.repositories

import android.util.Log
import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.model.User
import com.ravenzip.devicepicker.sources.UserSources
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class UserRepository @Inject constructor(private val userSources: UserSources) {
    suspend fun getUserData(userUid: String?) =
        flow {
                if (userUid != null) {
                    val response = userSources.userSourceByUid(userUid).get().await()
                    val convertedResponse = response.getValue<User>()
                    emit(convertedResponse ?: User())
                } else {
                    throw Exception("user is null")
                }
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getUserData", "${it.message}") }
                emit(User())
            }

    suspend fun createUserData(userUid: String?): Boolean {
        return try {
            if (userUid != null) {
                userSources.userSourceByUid(userUid).setValue(User()).await()
                true
            } else {
                throw Exception("user is null")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.e("createUserData", "${e.message}") }
            false
        }
    }
}
