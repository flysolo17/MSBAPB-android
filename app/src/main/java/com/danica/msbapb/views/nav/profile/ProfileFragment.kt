package com.danica.msbapb.views.nav.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentProfileBinding
import com.danica.msbapb.models.User
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.viewmodels.AuthViewModel


class ProfileFragment : Fragment() {

    private lateinit var _binding : FragmentProfileBinding
    private val _authViewModel by activityViewModels<AuthViewModel>()

    private var _users : User? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _authViewModel.users.observe(viewLifecycleOwner) {
            if (it is UiState.SUCCESS) {
                this._users =  it.data
                initInfo(it.data)
            }
        }
        _binding.buttonEditProfile.setOnClickListener {
            _users?.let {
                val directions = ProfileFragmentDirections.actionMenuProfileToEditProfileFragment(it)
                findNavController().navigate(directions)
            }
        }
        _binding.buttonChangePassword.setOnClickListener {
            _users?.let {
                val directions = ProfileFragmentDirections.actionMenuProfileToChangePasswordFragment(it)
                findNavController().navigate(directions)
            }
        }
        _binding.buttonTerms.setOnClickListener {
            openLink("https://msbapb.com/policies/terms.php")
        }
        _binding.buttonPrivacy.setOnClickListener {
            openLink("https://msbapb.com/policies/privacy.php")
        }
        _binding.buttonLogout.setOnClickListener {
            findNavController().navigate(R.id.bottom_logout)
        }
    }

    private fun initInfo(data: User) {
        Glide.with(_binding.root.context)
            .load(data.profile)
            .error(R.drawable.profile)
            .into(_binding.imageProfile)
        _binding.textFullname.text = data.fullname
        _binding.textAddress.text= data.address
        _binding.textPhone.text = data.phone
        _binding.textEmail.text = data.email
        _binding.textName.text =data.fullname
    }

    private fun openLink(url: String) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)

    }
}