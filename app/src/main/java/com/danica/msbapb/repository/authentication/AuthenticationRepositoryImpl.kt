package com.danica.msbapb.repository.authentication

import android.util.Log
import com.danica.msbapb.data.ResponseData

import com.danica.msbapb.services.AuthenticationService
import com.danica.msbapb.utils.UiState
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

const val TAG ="Authentication"
class AuthenticationRepositoryImpl(private val  authenticaionService : AuthenticationService): AuthenticationRepository {
    override  fun forgotPassword(email: String, result: (UiState<ResponseData<Any>>) -> Unit) {
        result.invoke(UiState.LOADING)
        authenticaionService.resetPassword(email).enqueue(object : Callback,retrofit2.Callback<ResponseData<Any>> {
            override fun onResponse(
                call: Call<ResponseData<Any>>,
                response: Response<ResponseData<Any>>
            ) {
                Log.d(TAG, "body: ${response.errorBody()}")
                Log.d(TAG, "error: ${response.raw()}")
                Log.d(TAG, "status: ${response.code()} ${response.message()}")
                Log.d(TAG, "status: ${response.code()} ${response.message()}")

                if (response.isSuccessful) {
                    val data: ResponseData<Any>? = response.body()
                    data?.let {
                        result.invoke(UiState.SUCCESS(it))
                    } ?: run {
                        result.invoke(UiState.FAILED(response.message()))
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
                val errorBody =t.toString()
                result.invoke(UiState.FAILED(errorBody ?: "Unknown error"))
                Log.d(TAG,t.toString(),t)
                Log.d(TAG,"onfail")
            }

        })
    }
}