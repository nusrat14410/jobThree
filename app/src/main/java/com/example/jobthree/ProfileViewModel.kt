package com.example.jobthree

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AppRepository(app)
    val uid = repo.currentUid()

    fun updateName(name: String, onDone: (Result<Unit>) -> Unit) = viewModelScope.launch {
        try {
            repo.updateDisplayName(uid!!, name)
            onDone(Result.success(Unit))
        } catch (e: Exception) { onDone(Result.failure(e)) }
    }

    fun updateLocation(onDone: (Result<Unit>) -> Unit) = viewModelScope.launch {
        try {
            val (lat, lng) = repo.getLastKnownLatLng() ?: throw IllegalStateException("No location")
            repo.updateLocation(uid!!, lat, lng)
            onDone(Result.success(Unit))
        } catch (e: Exception) { onDone(Result.failure(e)) }
    }
}