package com.danica.msbapb.repository.incidents

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.IncidentReport
import com.danica.msbapb.utils.UiState

interface IncidentReportRepository {

    suspend fun createIncidentReport(reporterId: Int,
                                    location: String,
                                    type: String,
                                    description: String,
                                    severity : Int,result : (UiState<ResponseData<Any>>) -> Unit)
    suspend fun getAllMyReports(uid : Int,result: (UiState<List<IncidentReport>>) -> Unit)
}