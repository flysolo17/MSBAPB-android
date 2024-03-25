package com.danica.msbapb.views.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
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
        lifecycleScope.launch {
            authViewModel.getUID().collect {
                if (it != 0) {
                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                }
            }
        }
    }
}