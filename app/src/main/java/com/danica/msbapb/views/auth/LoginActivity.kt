package com.danica.msbapb.views.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.danica.msbapb.MainActivity
import com.danica.msbapb.R
import com.danica.msbapb.databinding.ActivityLoginBinding
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->

            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {

            }
        }

    private fun checkPermission() {

        if (REQUIRED_PERMISSIONS.any {
                ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            activityResultLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }
    companion object {
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            authViewModel.authRepository.login(email,password) {
                when(it) {
                    is UiState.FAILED -> {
                        Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                        progress(false)
                    }
                    is UiState.LOADING -> progress(true)
                    is UiState.SUCCESS -> {
                        progress(false)
                        if (it.data.status) {
                            authViewModel.saveUID(it.data.data)
                            Toast.makeText(this,it.data.message,Toast.LENGTH_LONG).show()
                            startActivity(Intent(this,MainActivity::class.java))
                        }

                    }
                }
            }
        }
        binding.buttonForgotPassword.setOnClickListener {
          startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
        binding.buttonSignup.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }

    private fun progress(status : Boolean) {
        if (status) {
            binding.progressCircular.visibility = View.VISIBLE
            binding.buttonLogin.visibility = View.GONE
        } else {
            binding.progressCircular.visibility = View.GONE
            binding.buttonLogin.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        checkPermission()
        lifecycleScope.launch {
            authViewModel.getUID().collect {
                if (it != 0) {
                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                }
            }
        }
    }
}