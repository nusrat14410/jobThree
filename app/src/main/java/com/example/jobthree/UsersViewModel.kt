package com.example.jobthree

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.QuerySnapshot


class UsersViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = AppRepository(app)
    private val _users = MutableLiveData<List<AppUser>>(emptyList())
    val users: LiveData<List<AppUser>> = _users

    private var registration: com.google.firebase.firestore.ListenerRegistration? = null

    fun start() {
        registration?.remove()
        registration = repo.usersCollection().addSnapshotListener { snap, _ ->
            _users.value = snap.toUsers()
        }
    }

    fun stop() { registration?.remove(); registration = null }

    private fun QuerySnapshot?.toUsers(): List<AppUser> =
        this?.documents?.mapNotNull { it.toObject(AppUser::class.java) } ?: emptyList()

    fun logout() = repo.logout()
}