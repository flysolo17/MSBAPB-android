package com.danica.msbapb.services

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.IncidentReport
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface IncidentReportService {
    @Multipart
    @POST("api/reports/create.php")
    fun createIncidentReport(
        @Part("reporter_id",) reporterId: Int,
        @Part("location") location: RequestBody,
        @Part("type") type: RequestBody,
        @Part("description") description: RequestBody,
        @Part("status") status: Int = 1,
        @Part("severity") severity: Int,
        @Part("lat") latitude: Double,
        @Part("lng") longtitude: Double,
        @Part photo: MultipartBody.Part
    ): Call<ResponseData<Any>>

    @GET("api/reports/get_incidents_by_uid.php")
    fun getAllIncidents(@Query("uid") uid: Int): Call<List<IncidentReport>>
}