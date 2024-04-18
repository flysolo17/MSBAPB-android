package com.danica.msbapb.repository

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.User
import com.danica.msbapb.utils.UiState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody


interface AuthRepository {

    fun login(username : String , passwod : String,result : (UiState<ResponseData<Int>>) -> Unit)

    suspend fun saveUID(uid : Int)

    suspend fun getUID() : Flow<Int>

    suspend fun logout()
    fun getUserProfile(uid: Int,result : (UiState<User>) -> Unit)

    fun signUp(fullname : String,address : String ,phone : String ,email : String ,passwod: String ,result : (UiState<ResponseData<Int>>) -> Unit)
    fun editProfile(uid : Int,name : String,address: String,phone: String,result: (UiState<ResponseData<Any>>) -> Unit)
    fun changePassword(uid: Int,default : String ,new : String,confirm : String ,result: (UiState<ResponseData<Any>>) -> Unit)

   suspend fun uploadProfile(
        imageUri: MultipartBody.Part,
        id : Int,
        result : (UiState<ResponseData<Any>>) -> Unit
    )
}