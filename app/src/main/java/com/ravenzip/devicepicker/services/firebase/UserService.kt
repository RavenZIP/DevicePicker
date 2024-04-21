package com.ravenzip.devicepicker.services.firebase

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.ravenzip.devicepicker.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserService : ViewModel() {
    private val databaseRef = FirebaseDatabase.getInstance().getReference("Users")
    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    suspend fun get(currentUser: FirebaseUser?) =
        flow {
                if (currentUser != null) {
                    val response = databaseRef.child(currentUser.uid).get().await()
                    val data = response.getValue<User>()
                    if (data != null) {
                        _user.value = data
                    }
                    emit(true)
                } else {
                    throw Exception("currentUser is null")
                }
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("UserService: Get", "${it.message}") }
                emit(false)
            }

    suspend fun add(currentUser: FirebaseUser?) =
        flow {
                if (currentUser !== null) {
                    databaseRef.child(currentUser.uid).setValue(User()).await()
                    emit(true)
                } else {
                    throw Exception("currentUser is null")
                }
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("UserService: Add", "${it.message}") }
                emit(false)
            }

    suspend fun delete(currentUser: FirebaseUser?) {}
}
