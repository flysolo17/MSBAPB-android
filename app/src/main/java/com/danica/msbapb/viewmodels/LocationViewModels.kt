package com.danica.msbapb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.Locations
import com.danica.msbapb.models.Personels
import com.danica.msbapb.repository.adminservices.LocationRepository
import com.danica.msbapb.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModels @Inject constructor(private  val  locationRepository: LocationRepository)  : ViewModel(){


    private val _locations = MutableLiveData<UiState<ResponseData<List<Locations>>>>()
    val locations : LiveData<UiState<ResponseData<List<Locations>>>> get() = _locations

    fun getAllLocations() {
        viewModelScope.launch {
            locationRepository.getAllLocations {
                _locations.value = it
            }
        }
    }
}