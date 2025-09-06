package com.example.jobthree

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobthree.databinding.ActivityGoogleMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var b: ActivityGoogleMapBinding
    private lateinit var gmap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityGoogleMapBinding.inflate(layoutInflater)
        setContentView(b.root)
        setSupportActionBar(b.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFrag = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFrag.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        gmap = map
        FirebaseFirestore.getInstance().collection("AppUsers").get().addOnSuccessListener { snap ->
            val users = snap.documents.mapNotNull { it.toObject(AppUser::class.java) }
            var first: LatLng? = null
            users.forEach { u ->
                val lat = u.latitude; val lng = u.longitude
                if (lat != null && lng != null) {
                    val pos = LatLng(lat, lng)
                    if (first == null) first = pos
                    gmap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title(u.displayName ?: "(No name)")
                            .snippet(u.userEmail))
                }
            }
            first?.let { gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 12f)) }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}