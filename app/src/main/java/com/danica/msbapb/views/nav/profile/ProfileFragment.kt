package com.danica.msbapb.views.nav.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentProfileBinding
import com.danica.msbapb.viewmodels.AuthViewModel


class ProfileFragment : Fragment() {

    private lateinit var _binding : FragmentProfileBinding
    private val _authViewModel by activityViewModels<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}