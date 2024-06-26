package com.danica.msbapb.viewmodels


import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.User

import com.danica.msbapb.repository.AuthRepository
import com.danica.msbapb.repository.authentication.AuthenticationRepository
import com.danica.msbapb.utils.UiState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.Flow
import java.util.prefs.Preferences


import javax.inject.Inject

const val UID = "uid"
@HiltViewModel
class AuthViewModel @Inject constructor(val authRepository: AuthRepository,val authenticationRepository: AuthenticationRepository): ViewModel() {

    private var _users = MutableLiveData<UiState<User>>()
    val users: LiveData<UiState<User>>
        get() = _users



    private var _profile = MutableLiveData<UiState<ResponseData<Any>>>()
    val profile: LiveData<UiState<ResponseData<Any>>>
        get() = _profile

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
    fun saveProfile(context: Context,imageUri : Uri,id : Int) {
        val requestBody = context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
            }
        val filePart = requestBody?.let {
            MultipartBody.Part.createFormData("photo", "${System.currentTimeMillis()}.jpg", it)
        }
        if (filePart != null) {
            viewModelScope.launch {
                authRepository.uploadProfile(filePart,id) {
                    _profile.value = it
                }
            }
        }
    }
}