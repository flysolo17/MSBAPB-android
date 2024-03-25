package com.danica.msbapb.services

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.IncidentReport
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IncidentReportService {
    @FormUrlEncoded
    @POST("api/reports/create.php")
    fun createIncidentReport(
        @Field("reporter_id") reporterId: Int,
        @Field("location") location: String,
        @Field("type") type: String,
        @Field("description") description: String,
        @Field("status") status: Int = 1,
        @Field("severity") severity : Int
    ): Call<ResponseData<Any>>

    @GET("api/reports/get_incidents_by_uid.php")
    fun getAllIncidents(@Query("uid") uid: Int): Call<List<IncidentReport>>
}