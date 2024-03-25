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
import com.danica.msbapb.databinding.FragmentChangePasswordBinding
import com.danica.msbapb.databinding.FragmentEditProfileBinding
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.utils.confirmPassword
import com.danica.msbapb.utils.isPassword
import com.danica.msbapb.viewmodels.AuthViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePasswordFragment : Fragment() {
    private val _args by navArgs<ChangePasswordFragmentArgs>()
    private lateinit var _binding : FragmentChangePasswordBinding
    private val _authViewModel by activityViewModels<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater,container,false)
        _binding.textEmail.text = _args.users.email
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        _binding.btnChangePassword.setOnClickListener {
            val  uid = _args.users.id
            val default = _binding.inputDefault.text.toString()
            val new = _binding.inputPassword.text.toString()
            val confirm = _binding.inputConfirmpassword.text.toString()
            if (!_binding.layoutDefault.isPassword() || !_binding.layoutPassword.isPassword() ||  !_binding.layoutConfirmPassword.confirmPassword(new)) {
                return@setOnClickListener
            }
            changePassword(uid, default, new, confirm)
        }
    }

    private fun changePassword(uid : Int,default : String,new : String,confirm : String) {
        _authViewModel.authRepository.changePassword(uid,default,new,confirm) {
            when(it) {
                is UiState.FAILED -> {
                    progress(false)
                    Toast.makeText(_binding.root.context,it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.LOADING -> progress(true)
                is UiState.SUCCESS -> {
                    progress(false)
                    if (it.data.status) {
                        Toast.makeText(_binding.root.context,it.data.message, Toast.LENGTH_SHORT).show()
                        _binding.layoutDefault.error = it.data.message
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(_binding.root.context,it.data.message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
    fun progress(progress : Boolean) {
        if (progress) {
            _binding.circleProgress.visibility = View.VISIBLE
            _binding.btnChangePassword.visibility = View.GONE
        } else {
            _binding.circleProgress.visibility = View.GONE
            _binding.btnChangePassword.visibility = View.VISIBLE
        }
    }
}