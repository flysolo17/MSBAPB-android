package com.danica.msbapb.repository.authentication

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.utils.UiState

interface AuthenticationRepository {
     fun forgotPassword(email : String,result : (UiState<ResponseData<Any>>) -> Unit)
}