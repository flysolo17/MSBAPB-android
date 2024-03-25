package com.danica.msbapb.views.nav.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentEditProfileBinding
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.utils.isPhone
import com.danica.msbapb.utils.isString
import com.danica.msbapb.viewmodels.AuthViewModel



class EditProfileFragment : Fragment() {
    private val _args by navArgs<EditProfileFragmentArgs>()
    private lateinit var _binding : FragmentEditProfileBinding
    private val _authViewModel by activityViewModels<AuthViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater,container,false)
        _binding.inputFullname.setText(_args.users.fullname)
        _binding.inputAddress.setText(_args.users.address)
        _binding.inputPhone.setText(_args.users.phone)
        _binding.textEmail.text = _args.users.email
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.btnSave.setOnClickListener {
            val uid = _args.users.id
            val name = _binding.inputFullname.text.toString()
            val phone =_binding.inputPhone.text.toString()
            val address = _binding.inputAddress.text.toString()

            if (!_binding.layoutFullname.isString() || !_binding.layoutPhone.isPhone() || !_binding.layoutAddress.isString()) {
                return@setOnClickListener
            }
            updateProfile(uid, name, phone, address)
        }
    }

    private fun updateProfile(uid : Int, name : String, phone : String, address : String) {
        _authViewModel.authRepository.editProfile(uid,name,address,phone) {
            when(it) {
                is UiState.FAILED -> {
                    progress(false)
                    Toast.makeText(_binding.root.context,it.message,Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> progress(true)
                is UiState.SUCCESS -> {
                    progress(true)
                    Toast.makeText(_binding.root.context,it.data.message,Toast.LENGTH_SHORT).show()
                    _authViewModel.getUserProfile(uid)
                    findNavController().popBackStack()
                }
            }
        }
    }

    fun progress(progress : Boolean) {
        if (progress) {
            _binding.circleProgress.visibility = View.VISIBLE
            _binding.btnSave.visibility = View.GONE
        } else {
            _binding.circleProgress.visibility = View.GONE
            _binding.btnSave.visibility = View.VISIBLE
        }
    }

}