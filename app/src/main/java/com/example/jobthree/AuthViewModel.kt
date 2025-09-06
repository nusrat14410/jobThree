package com.example.jobthree

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AppRepository(app)

    private val _state = MutableLiveData<Result<Unit>>()
    val state: LiveData<Result<Unit>> = _state

    fun login(email: String, pass: String) = viewModelScope.launch {
        try {
            repo.login(email, pass)
            ensureUserSaved()
            _state.value = Result.success(Unit)
        } catch (e: Exception) { _state.value = Result.failure(e) }
    }

    fun register(email: String, pass: String) = viewModelScope.launch {
        try {
            repo.register(email, pass)
            ensureUserSaved()
            _state.value = Result.success(Unit)
        } catch (e: Exception) { _state.value = Result.failure(e) }
    }

    private suspend fun ensureUserSaved() {
        val uid = repo.currentUid() ?: return
        val email = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.email.orEmpty()
        val loc = repo.getLastKnownLatLng()
        val user = AppUser(
            uid = uid,
            userEmail = email,
            displayName = null,
            latitude = loc?.first,
            longitude = loc?.second
        )
        repo.saveUser(user)
    }
}