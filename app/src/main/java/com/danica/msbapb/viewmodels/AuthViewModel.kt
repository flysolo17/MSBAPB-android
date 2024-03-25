package com.danica.msbapb.viewmodels


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danica.msbapb.models.User

import com.danica.msbapb.repository.AuthRepository
import com.danica.msbapb.repository.authentication.AuthenticationRepository
import com.danica.msbapb.utils.UiState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Flow
import java.util.prefs.Preferences


import javax.inject.Inject

const val UID = "uid"
@HiltViewModel
class AuthViewModel @Inject constructor(val authRepository: AuthRepository,val authenticationRepository: AuthenticationRepository): ViewModel() {

    private var _users = MutableLiveData<UiState<User>>()
    val users: LiveData<UiState<User>>
        get() = _users

    fun saveUID(uid : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.saveUID(uid)
        }
    }

    fun getUID() = runBlocking {
        authRepository.getUID()
    }
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
    fun getUserProfile(uid: Int) {
        authRepository.getUserProfile(uid) {
            _users.value = it
        }
    }
}