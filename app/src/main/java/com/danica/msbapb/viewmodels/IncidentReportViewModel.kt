package com.danica.msbapb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danica.msbapb.models.IncidentReport
import com.danica.msbapb.repository.incidents.IncidentReportRepository
import com.danica.msbapb.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IncidentReportViewModel @Inject constructor(val incidentReportRepository: IncidentReportRepository): ViewModel() {

    private val _incidents = MutableLiveData<UiState<List<IncidentReport>>>()
    val incidents : LiveData<UiState<List<IncidentReport>>> get() = _incidents

    fun getAllIncidents(uid : Int) {
        viewModelScope.launch {
            incidentReportRepository.getAllMyReports(uid) {
                _incidents.postValue(it)
            }
        }
    }

}