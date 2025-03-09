package com.ravenzip.devicepicker.common.repositories

import android.util.Log
import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.common.model.UserDto
import com.ravenzip.devicepicker.common.sources.UserSources
import com.ravenzip.kotlinflowextended.functions.materialize
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class UserRepository
@Inject
constructor(private val authRepository: AuthRepository, private val userSources: UserSources) {
    fun getUserData() =
        flow {
                val userUid = authRepository.firebaseUser?.uid

                if (userUid != null) {
                    val response = userSources.currentUser(userUid).get().await()
                    val convertedResponse = response.getValue<UserDto>()

                    if (convertedResponse == null) {
                        throw Exception("При загрузке данных о пользователе произошла ошибка")
                    }

                    emit(convertedResponse)
                } else {
                    throw Exception("Ошибка авторизации пользователя")
                }
            }
            .materialize()

    suspend fun createUserData(): Boolean {
        return try {
            val userUid = authRepository.firebaseUser?.uid

            if (userUid != null) {
                userSources.currentUser(userUid).setValue(UserDto()).await()
                true
            } else {
                throw Exception("Ошибка авторизации пользователя")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.e("createUserData", "${e.message}") }
            false
        }
    }

    suspend fun updateDeviceHistory(deviceHistory: List<String>): Boolean {
        return try {
            val userUid = authRepository.firebaseUser?.uid

            if (userUid != null) {
                userSources.deviceHistory(userUid).setValue(deviceHistory).await()
                true
            } else {
                throw Exception("Ошибка авторизации пользователя")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.e("updateDeviceHistory", "${e.message}") }
            false
        }
    }

    suspend fun updateFavourites(favourites: List<String>): Boolean {
        return try {
            val userUid = authRepository.firebaseUser?.uid

            if (userUid != null) {
                userSources.favourites(userUid).setValue(favourites).await()
                true
            } else {
                throw Exception("Ошибка авторизации пользователя")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.e("updateFavourites", "${e.message}") }
            false
        }
    }

    suspend fun updateCompares(compares: List<String>): Boolean {
        return try {
            val userUid = authRepository.firebaseUser?.uid

            if (userUid != null) {
                userSources.compares(userUid).setValue(compares).await()
                true
            } else {
                throw Exception("Ошибка авторизации пользователя")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { Log.e("updateCompares", "${e.message}") }
            false
        }
    }

    fun updateCompanyUid(companyUid: String) =
        flow {
                val userUid = authRepository.firebaseUser?.uid

                if (userUid == null) {
                    throw Exception("Ошибка авторизации пользователя")
                }

                userSources.companyUid(userUid).setValue(companyUid).await()

                emit(companyUid)
            }
            .materialize()
}
