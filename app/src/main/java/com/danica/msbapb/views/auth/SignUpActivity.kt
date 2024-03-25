package com.danica.msbapb.views.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.danica.msbapb.R
import com.danica.msbapb.databinding.ActivitySignUpBinding
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.utils.confirmPassword
import com.danica.msbapb.utils.isEmail
import com.danica.msbapb.utils.isPassword
import com.danica.msbapb.utils.isPhone
import com.danica.msbapb.utils.isString
import com.danica.msbapb.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var _binding : ActivitySignUpBinding
    private val _authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        _binding.btnBack.setOnClickListener {
            finish()
        }
        _binding.btnSignIn.setOnClickListener {
            finish()
        }

        _binding.btnSignUp.setOnClickListener {
            val fullname = _binding.inputFullname.text.toString()
            val address = _binding.inputAddress.text.toString()
            val phone = _binding.inputPhone.text.toString()
            val email = _binding.inputEmail.text.toString()
            val password = _binding.inputPassword.text.toString()
            if (!_binding.layoutFullname.isString() || !_binding.layoutEmail.isEmail() || !_binding.layoutPhone.isPhone() || !_binding.layoutPassword.isPassword() || !_binding.layoutConfirmPassword.confirmPassword(password)) {
                return@setOnClickListener
            }

            signUp(fullname, address, phone, email, password)
        }
    }

    private fun signUp(fullname : String,address : String,phone : String ,email : String ,password : String) {
        _authViewModel.authRepository.signUp(fullname,address,phone,email,password) {
            when(it) {
                is UiState.FAILED -> {
                    _binding.circleProgress.visibility = View.GONE
                    _binding.btnSignUp.visibility = View.VISIBLE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                is UiState.LOADING -> {
                    _binding.btnSignUp.visibility = View.GONE
                    _binding.circleProgress.visibility = View.VISIBLE

                }
                is UiState.SUCCESS -> {
                    _binding.circleProgress.visibility = View.GONE
                    _binding.btnSignUp.visibility = View.VISIBLE
                    Toast.makeText(this,it.data.message,Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}