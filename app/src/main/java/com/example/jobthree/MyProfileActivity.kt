package com.example.jobthree

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobthree.databinding.ActivityMyProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MyProfileActivity : AppCompatActivity() {
    private lateinit var b: ActivityMyProfileBinding
    private val vm: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(b.root)
        setSupportActionBar(b.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val user = FirebaseAuth.getInstance().currentUser
        b.tvEmail.text = user?.email ?: ""
        b.etDisplayName.setText(user?.displayName ?: "")

        b.btnSave.setOnClickListener {
            val name = b.etDisplayName.text.toString().trim()
            vm.updateName(name) { result ->
                val msg = result.exceptionOrNull()?.message ?: "Updated!"
                Snackbar.make(b.root, msg, Snackbar.LENGTH_LONG).show()
            }
        }

        b.btnUpdateLocation.setOnClickListener {
            vm.updateLocation { result ->
                val msg = result.exceptionOrNull()?.message ?: "Location updated!"
                Snackbar.make(b.root, msg, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}