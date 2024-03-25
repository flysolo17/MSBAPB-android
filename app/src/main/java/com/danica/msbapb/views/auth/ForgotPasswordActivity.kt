package com.danica.msbapb.views.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.danica.msbapb.R
import com.danica.msbapb.databinding.ActivityForgotPasswordBinding
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.utils.isEmail
import com.danica.msbapb.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    private  lateinit var  _binding :ActivityForgotPasswordBinding
    private val _authViewModel by viewModels<AuthViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        _binding.btnForgotPassword.setOnClickListener {
            val email = _binding.inputEmail.text.toString()
            if (!_binding.layoutEmail.isEmail()) {
                return@setOnClickListener
            }
            forgotPassword(email)
        }



    }


    private fun forgotPassword(email :String ){
        _authViewModel.authenticationRepository.forgotPassword(email) {
            when(it) {
                is UiState.FAILED -> {
                    Toast.makeText(this@ForgotPasswordActivity,it.message,Toast.LENGTH_LONG).show()
                    progress(false)
                }
                is UiState.LOADING -> progress(true)
                is UiState.SUCCESS -> {
                    progress(false)
                    Toast.makeText(this@ForgotPasswordActivity,it.data.message,Toast.LENGTH_LONG).show()
                }
            }
        }

    }
    private fun progress(status : Boolean) {
        if (status) {
            _binding.circleProgress.visibility = View.VISIBLE
            _binding.btnForgotPassword.visibility = View.GONE
        } else {
            _binding.circleProgress.visibility = View.GONE
            _binding.btnForgotPassword.visibility = View.VISIBLE
        }
    }

}