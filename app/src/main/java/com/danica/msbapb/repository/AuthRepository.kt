package com.danica.msbapb.repository

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.User
import com.danica.msbapb.utils.UiState
import kotlinx.coroutines.flow.Flow


interface AuthRepository {

    fun login(username : String , passwod : String,result : (UiState<ResponseData<Int>>) -> Unit)

    suspend fun saveUID(uid : Int)

    suspend fun getUID() : Flow<Int>

    suspend fun logout()
    fun getUserProfile(uid: Int,result : (UiState<User>) -> Unit)
}