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
    @POST("api/auth/login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseData<Int>>

    @GET("api/auth/get_user.php")
    fun getUser(
        @Query("uid") uid: Int,
    ): Call<ResponseData<User>>



    @FormUrlEncoded
    @POST("api/auth/update_profile.php")
    fun updateProfile(
        @Query("uid") uid: Int,
        @Field("fullname") name : String,
        @Field("address") address: String,
        @Field("phone") phone : String,
    ): Call<ResponseData<Any>>



    @FormUrlEncoded
    @POST("api/auth/change_password.php")
    fun changePassword(
        @Query("uid") uid: Int,
        @Field("default") default : String,
        @Field("new") new: String,
        @Field("confirm") confirm : String,
    ): Call<ResponseData<Any>>


    @FormUrlEncoded
    @POST("api/auth/sign_up.php")
    fun createUser(
        @Field("fullname") fullname: String,
        @Field("address") address: String,
        @Field("cp_number") cp_number: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseData<Int>>
}