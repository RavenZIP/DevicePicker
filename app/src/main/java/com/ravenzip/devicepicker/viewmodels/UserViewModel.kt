package com.ravenzip.devicepicker.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.ravenzip.devicepicker.data.User
import com.ravenzip.devicepicker.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    suspend fun get(currentUser: FirebaseUser?) =
        flow {
                if (currentUser != null) {
                    _user.value = userRepository.getUser(currentUser.uid)
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
                    userRepository.addUser(currentUser.uid)
                    emit(true)
                } else {
                    throw Exception("currentUser is null")
                }
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("UserService: Add", "${it.message}") }
                emit(false)
            }
}
