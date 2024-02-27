package com.danica.msbapb.services

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.IncidentReport
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface IncidentReportService {
    @FormUrlEncoded
    @POST("reports/create.php")
    fun createIncidentReport(
        @Field("reporter_id") reporterId: Int,
        @Field("location") location: String,
        @Field("type") type: String,
        @Field("description") description: String,
        @Field("status") status: Int = 1,
        @Field("severity") severity : Int
    ): Call<ResponseData<Any>>

    @GET("reports/incidents.php")
    fun getAllIncidents() :Call<List<IncidentReport>>
}