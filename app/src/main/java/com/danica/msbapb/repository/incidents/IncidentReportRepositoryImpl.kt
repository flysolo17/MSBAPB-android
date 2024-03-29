package com.danica.msbapb.repository.incidents

import android.util.Log
import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.IncidentReport
import com.danica.msbapb.services.IncidentReportService
import com.danica.msbapb.utils.UiState
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import javax.security.auth.callback.Callback
const val INCIDENT_TAG = "incidents"
class IncidentReportRepositoryImpl(private val reportService: IncidentReportService): IncidentReportRepository {
    override suspend  fun createIncidentReport(reporterId: Int, location: String, type: String, description: String, severity : Int,result : (UiState<ResponseData<Any>>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        reportService.createIncidentReport(reporterId,location,type,description, 1,severity).enqueue(object  : Callback,
            retrofit2.Callback<ResponseData<Any>> {
            override fun onResponse(
                call: Call<ResponseData<Any>>,
                response: Response<ResponseData<Any>>
            ) {
                if (response.isSuccessful) {
                  val data = response.body()
                    data?.let {
                        result.invoke(UiState.SUCCESS(it))
                    }
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    errorBodyString?.let {
                        try {
                            val errorJson = JSONObject(it)
                            val errorMessage = errorJson.getString("message")
                            result.invoke(UiState.FAILED(errorMessage))
                        } catch (e: JSONException) {
                            result.invoke(UiState.FAILED("Error parsing error message"))
                        }
                    } ?: run {
                        result.invoke(UiState.FAILED("Unknown error"))
                    }
                }
            }
            override fun onFailure(call: Call<ResponseData<Any>>, t: Throwable) {
                result.invoke(UiState.FAILED(t.message.toString()))
            }
        })
    }

    override suspend fun getAllMyReports(uid: Int, result: (UiState<List<IncidentReport>>) -> Unit) {
        result.invoke(UiState.LOADING)
        reportService.getAllIncidents(uid).enqueue(object  : Callback,
            retrofit2.Callback<List<IncidentReport>> {
            override fun onResponse(
                call: Call<List<IncidentReport>>,
                response: Response<List<IncidentReport>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    Log.d(INCIDENT_TAG,data.toString())
                    result.invoke(UiState.SUCCESS(data))

                } else {
                    val errorBodyString = response.errorBody()?.string()
                    errorBodyString?.let {
                        try {
                            val errorJson = JSONObject(it)
                            val errorMessage = errorJson.getString("message")
                            result.invoke(UiState.FAILED(errorMessage))
                        } catch (e: JSONException) {
                            result.invoke(UiState.FAILED("Error parsing error message"))
                        }
                    } ?: run {
                        result.invoke(UiState.FAILED("Unknown error"))
                    }
                }
            }
            override fun onFailure(call: Call<List<IncidentReport>>, t: Throwable) {
                result.invoke(UiState.FAILED(t.message.toString()))
            }

        })
    }


}