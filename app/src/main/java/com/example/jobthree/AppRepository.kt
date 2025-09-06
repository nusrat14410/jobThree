package com.example.jobthree

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AppRepository(private val context: Context) {

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    fun currentUid(): String? = auth.currentUser?.uid
    fun isLoggedIn(): Boolean = currentUid() != null

    suspend fun register(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password).await()

    suspend fun login(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password).await()

    fun logout() = auth.signOut()

    suspend fun saveUser(user: AppUser) {
        requireNotNull(user.uid)
        db.collection("AppUsers").document(user.uid).set(user).await()
    }

    suspend fun updateDisplayName(uid: String, name: String) {
        db.collection("AppUsers").document(uid).update(mapOf("displayName" to name, "updatedAt" to System.currentTimeMillis())).await()
    }

    suspend fun updateLocation(uid: String, lat: Double, lng: Double) {
        db.collection("AppUsers").document(uid).update(mapOf(
            "latitude" to lat, "longitude" to lng, "updatedAt" to System.currentTimeMillis()
        )).await()
    }

    fun usersCollection() = db.collection("AppUsers")

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLatLng(): Pair<Double, Double>? {
        val provider = LocationServices.getFusedLocationProviderClient(context)
        val loc = provider.lastLocation.await() ?: return null
        return loc.latitude to loc.longitude
    }
}