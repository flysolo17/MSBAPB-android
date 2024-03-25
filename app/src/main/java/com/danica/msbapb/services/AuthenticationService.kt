package com.danica.msbapb.services

import com.danica.msbapb.data.ResponseData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthenticationService {
    @FormUrlEncoded
    @POST("src/forgot-password.php")
    fun resetPassword(
        @Field("email") email: String,
    ): Call<ResponseData<Any>>
}


