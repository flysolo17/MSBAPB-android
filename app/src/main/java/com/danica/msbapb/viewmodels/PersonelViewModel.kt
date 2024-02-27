package com.danica.msbapb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danica.msbapb.models.IncidentReport
import com.danica.msbapb.models.Personels
import com.danica.msbapb.repository.perosnels.PersonelRepository
import com.danica.msbapb.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonelViewModel @Inject constructor(private  val personelRepository: PersonelRepository) : ViewModel() {

    private val _personels = MutableLiveData<UiState<List<Personels>>>()
    val personels : LiveData<UiState<List<Personels>>> get() = _personels

    fun getAllPersonels() {
        viewModelScope.launch {
            personelRepository.getAllPersonels {
                _personels.value = it
            }
        }
    }
}