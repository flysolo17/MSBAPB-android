package com.danica.msbapb.views.nav.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentCreateReportBinding
import com.danica.msbapb.utils.UiState
import com.danica.msbapb.viewmodels.IncidentReportViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateReportFragment : Fragment() {
    private lateinit var _binding : FragmentCreateReportBinding
    private val incidentReportViewModel by activityViewModels<IncidentReportViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateReportBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.buttonSaveReport.setOnClickListener {
            lifecycleScope.launch {
                incidentReportViewModel.incidentReportRepository.createIncidentReport(
                    6,"Tagatay", "banggaan", "May Nagsuntukan sa daan",4
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
}