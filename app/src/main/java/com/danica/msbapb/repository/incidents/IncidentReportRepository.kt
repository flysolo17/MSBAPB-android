package com.danica.msbapb.repository.incidents

import android.net.Uri
import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.IncidentReport
import com.danica.msbapb.utils.UiState
import okhttp3.MultipartBody

import retrofit2.http.Multipart
import retrofit2.http.Part

interface IncidentReportRepository {

    suspend fun createIncidentReport(reporterId: Int,
                                     location: String,
                                     type: String,
                                     description: String,
                                     severity : Int,
                                     imageUri: MultipartBody.Part,
                                     result : (UiState<ResponseData<Any>>) -> Unit)
    suspend fun getAllMyReports(uid : Int,result: (UiState<List<IncidentReport>>) -> Unit)
}