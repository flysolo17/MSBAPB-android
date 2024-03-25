package com.danica.msbapb.views.nav.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentCreateReportBinding
import com.danica.msbapb.models.User
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.viewmodels.AuthViewModel
import com.danica.msbapb.viewmodels.IncidentReportViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val incidentTypes = arrayOf("Accident", "Fire", "Robbery", "Medical Emergency", "Natural Disaster","Other")
private val incidentSeverity= arrayOf("LOW", "MEDIUM", "HIGH")
class CreateReportFragment : Fragment() {
    private lateinit var _binding : FragmentCreateReportBinding
    private val incidentReportViewModel by activityViewModels<IncidentReportViewModel>()
    private val _authViewModel by  activityViewModels<AuthViewModel>()
    private var _user : User ? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReportBinding.inflate(inflater,container,false)
        val adapter = ArrayAdapter(_binding.root.context, android.R.layout.simple_dropdown_item_1line, incidentTypes)
        _binding.autoCompleteTextView.setAdapter(adapter)

        val adapter2 = ArrayAdapter(_binding.root.context, android.R.layout.simple_dropdown_item_1line, incidentSeverity)
        _binding.inputSeverity.setAdapter(adapter2)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _authViewModel.users.observe(viewLifecycleOwner) {
            if (it is UiState.SUCCESS) {
                _user = it.data
            }
        }
        _binding.buttonSaveReport.setOnClickListener {
            val reporterID = _user?.id ?: 0
            val location = _binding.inputLocation.text.toString()
            val description = _binding.inputDescription.text.toString()
            val type = _binding.autoCompleteTextView.text.toString()
            val severity = _binding.inputSeverity.text.toString().getServerity()
            if (location.isEmpty()) {
                _binding.layoutLocation.error = "Enter location"
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                _binding.layoutDescription.error = "Enter description"
                return@setOnClickListener
            }
            if (type.isEmpty()) {
                _binding.layoutType.error = "Enter incident Type"
                return@setOnClickListener
            }
            if (severity <= 0) {
                _binding.layoutSeverity.error = "Enter severity"
                return@setOnClickListener
            }
            saveReport(reporterID, location, description, type, severity)
        }
    }


    fun saveReport(reporterID : Int,location : String,description : String,type : String,severity : Int) {
        lifecycleScope.launch {
            incidentReportViewModel.incidentReportRepository.createIncidentReport(
                reporterID,location, type, description,severity
            ) {
                when(it) {
                    is UiState.FAILED -> progress(false)
                    is UiState.LOADING -> progress(true)
                    is UiState.SUCCESS -> {
                        progress(false)
                        Toast.makeText(_binding.root.context,it.data.message,Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
    private fun progress(status : Boolean) {
        if (status) {
            _binding.progressCircular.visibility = View.VISIBLE
            _binding.buttonSaveReport.visibility = View.GONE
        } else {
            _binding.progressCircular.visibility = View.GONE
            _binding.buttonSaveReport.visibility = View.VISIBLE
        }
    }

    fun String.getServerity() : Int {
        return if (this == "LOW") {
            1
        } else if (this == "MEDIUM") {
            2
        } else {
            4
        }
    }
}