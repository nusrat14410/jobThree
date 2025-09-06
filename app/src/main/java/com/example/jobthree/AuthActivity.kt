package com.example.jobthree

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.jobthree.databinding.ActivityAuthBinding
import com.google.android.material.snackbar.Snackbar


class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val vm: AuthViewModel by viewModels()

    private val requestLoc = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* ignore result; we’ll re-check when saving location */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestLocationPermsIfNeeded()

        binding.btnLogin.setOnClickListener {
            val e = binding.etEmail.text.toString().trim()
            val p = binding.etPassword.text.toString()
            vm.login(e, p)
        }
        binding.btnRegister.setOnClickListener {
            val e = binding.etEmail.text.toString().trim()
            val p = binding.etPassword.text.toString()
            vm.register(e, p)
        }

        vm.state.observe(this) { result ->
            result.onSuccess {
                startActivity(Intent(this, FriendListActivity::class.java))
                finish()
            }.onFailure {
                Snackbar.make(binding.root, it.message ?: "Auth failed", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun requestLocationPermsIfNeeded() {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (fine != PackageManager.PERMISSION_GRANTED) {
            requestLoc.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }
}