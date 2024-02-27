package com.danica.msbapb.services

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @FormUrlEncoded
    @POST("auth/login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseData<Int>>

    @GET("auth/get_user.php")
    fun getUser(
        @Query("uid") uid: Int,
    ): Call<ResponseData<User>>
}