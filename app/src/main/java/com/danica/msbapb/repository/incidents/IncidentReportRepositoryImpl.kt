package com.danica.msbapb.repository.incidents

import android.util.Log
import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.IncidentReport
import com.danica.msbapb.services.IncidentReportService
import com.danica.msbapb.utils.UiState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

const val INCIDENT_TAG = "incidents"
class IncidentReportRepositoryImpl(private val reportService: IncidentReportService): IncidentReportRepository {
    override suspend  fun createIncidentReport(reporterId: Int, location: String, type: String, description: String, severity : Int, imageUri : MultipartBody.Part, result : (UiState<ResponseData<Any>>) -> Unit
    ) {
        result.invoke(UiState.LOADING)

        val param2 = createPartFromString(location)
        val param3 = createPartFromString(type)
        val param4 = createPartFromString(description)

        reportService.createIncidentReport(reporterId,param2,param3,param4, 1,severity, imageUri).enqueue(object  : Callback,
            retrofit2.Callback<ResponseData<Any>> {
            override fun onResponse(
                call: Call<ResponseData<Any>>,
                response: Response<ResponseData<Any>>
            ) {
                Log.d(INCIDENT_TAG,response.errorBody().toString())
                if (response.isSuccessful) {
                  val data = response.body()
                  data?.let {
                      result.invoke(UiState.SUCCESS(it))
                      Log.d(INCIDENT_TAG,data.toString())
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
                        Log.d(INCIDENT_TAG,errorBodyString)
                    } ?: run {
                        result.invoke(UiState.FAILED("Unknown error"))
                    }
                }
            }
            override fun onFailure(call: Call<ResponseData<Any>>, t: Throwable) {
                result.invoke(UiState.FAILED(t.message.toString()))
                Log.d(INCIDENT_TAG,t.message.toString())
            }
        })
    }

    fun createPartFromString(stringData: String): RequestBody {
        return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
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