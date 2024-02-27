package com.danica.msbapb.views.bottom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentLogoutBinding
import com.danica.msbapb.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutFragment : DialogFragment() {
    private val  _authViewModel by activityViewModels<AuthViewModel>()
    private lateinit var _binding  : FragmentLogoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLogoutBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.buttonLogout.setOnClickListener {
            _authViewModel.logout().also {
                Toast.makeText(_binding.root.context,"Successfully Logged out!",Toast.LENGTH_LONG).show()
                activity?.finish()
            }
        }
        _binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }

}