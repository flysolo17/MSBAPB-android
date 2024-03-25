package com.danica.msbapb.services

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.Personels
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface PersonelService {

    @GET("api/auth/personels/get_all_personels.php")
    fun getAllPersonels(): Call<List<Personels>>
}