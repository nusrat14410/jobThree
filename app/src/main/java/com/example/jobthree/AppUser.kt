package com.example.jobthree

data class AppUser(
    val uid: String = "",
    val userEmail: String = "",
    val displayName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
