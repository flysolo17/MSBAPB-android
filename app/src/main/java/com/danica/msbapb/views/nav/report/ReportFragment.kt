package com.danica.msbapb.views.nav.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentReportBinding
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.viewmodels.AuthViewModel
import com.danica.msbapb.viewmodels.IncidentReportViewModel
import com.danica.msbapb.views.adapters.IncidentReportAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private lateinit var _binding : FragmentReportBinding
    private val _authViewModel by activityViewModels<AuthViewModel>()
    private val _incidentReportViewModel by activityViewModels<IncidentReportViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.buttonCreateReport.setOnClickListener {
            findNavController().navigate(R.id.action_menu_incident_to_createReportFragment)
        }

        _incidentReportViewModel.getAllIncidents()
        observers()
    }
    private fun observers() {
        _incidentReportViewModel.incidents.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.FAILED -> progress(false)
                is UiState.LOADING -> progress(true)
                is UiState.SUCCESS -> {
                    progress(false)
                    _binding.recyclerviewReports.apply {
                        layoutManager = LinearLayoutManager(_binding.root.context)
                        adapter = IncidentReportAdapter(_binding.root.context,it.data.reversed())
                    }
                }
            }
        }
    }

    private fun progress(status : Boolean) {
        if (status) {
            _binding.loadingScreen.loadingScreen.visibility = View.VISIBLE
            _binding.recyclerviewReports.visibility = View.GONE
        }  else {
            _binding.loadingScreen.loadingScreen.visibility = View.GONE
            _binding.recyclerviewReports.visibility = View.VISIBLE
        }
    }

}