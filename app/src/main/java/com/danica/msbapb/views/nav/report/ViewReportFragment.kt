package com.danica.msbapb.views.nav.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.danica.msbapb.R
import com.danica.msbapb.databinding.FragmentViewReportBinding
import com.danica.msbapb.models.IncidentReport
import com.danica.msbapb.utils.STORAGE_LINK


class ViewReportFragment : Fragment() {
    private lateinit var _binding : FragmentViewReportBinding
   private val args by navArgs<ViewReportFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewReportBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.incident.run {
            if (this.photo != null) {
                Glide.with(view.context).load("$STORAGE_LINK/${this.photo}").error(R.drawable.news).into(_binding.reportImage)
            }
            _binding.textDescription.text = "Description: ${this.description}"
            _binding.textLocation.text = "Location: ${this.location}"
            _binding.textIncidentSeverity.text = "Severity: ${this.severity}"
            _binding.textIncidentDate.text = "Date: ${this.incident_date}"
            _binding.textIncidentType.text = "Type: ${this.type}"
            _binding.textIncidentStatus.text = "Status: ${this.status}"
            _binding.textRespondents.text = this.respondents ?: ""
        }

    }


}