package com.danica.msbapb.services

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.Locations
import com.danica.msbapb.models.Personels
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface LocationService {

    @GET("api/admin/get_all_locations.php")
     fun getAllLocations(): Call<ResponseData<List<Locations>>>
}