package com.danica.msbapb.repository

import android.content.Context
import android.util.Log

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.danica.msbapb.R
import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.User
import com.danica.msbapb.services.AuthService
import com.danica.msbapb.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

import javax.security.auth.callback.Callback

private val Context.dataStore by preferencesDataStore(
    name = "user_key"
)
const val TAG = "AuthRepository"


class AuthRepositoryImpl(context: Context, private val  authService: AuthService) : AuthRepository {
    private val dataStore = context.dataStore
    private val USER_KEY = intPreferencesKey(context.getString(R.string.user_key))


    override fun login(
        username: String,
        passwod: String,
        result: (UiState<ResponseData<Int>>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        authService.login(username,passwod).enqueue(object : Callback,retrofit2.Callback<ResponseData<Int>> {
            override fun onResponse(
                call: Call<ResponseData<Int>>,
                response: Response<ResponseData<Int>>
            ) {
                Log.d(TAG, "body: ${response.body()}")
                Log.d(TAG, "error: ${response.errorBody()}")
                Log.d(TAG, "status: ${response.code()} ${response.message()}")

                if (response.isSuccessful) {
                    val data : ResponseData<Int> ?= response.body()
                    data?.let {
                        result.invoke(UiState.SUCCESS(it))
                        return@let
                    } ?: run {
                        result.invoke(UiState.FAILED(response.message()))
                    }
                    Log.d(TAG,"If")

                } else {
                    Log.d(TAG,"else")
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
            override fun onFailure(call: Call<ResponseData<Int>>, t: Throwable) {
                result.invoke(UiState.FAILED(t.message.toString()))
                Log.d(TAG,t.toString(),t)
            }
        })
    }
    override suspend fun saveUID(uid : Int) {
        dataStore.edit { settings ->
            settings[USER_KEY] = uid
        }
    }

    override suspend fun getUID(): Flow<Int> {
       return dataStore.data
            .catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                }
            }
            .map { preferences ->
                preferences[USER_KEY] ?: 0 }
    }

    override suspend fun logout() {
        dataStore.edit {settings ->
            settings.clear()
        }
    }

    override fun getUserProfile(uid: Int, result: (UiState<User>) -> Unit) {
        result.invoke(UiState.LOADING)
        authService.getUser(uid).enqueue(object  : Callback ,
            retrofit2.Callback<ResponseData<User>> {
            override fun onResponse(
                call: Call<ResponseData<User>>,
                response: Response<ResponseData<User>>
            ) {
                Log.d(TAG,response.body().toString())
                if (response.isSuccessful) {
                    val data : ResponseData<User> ?= response.body()
                    data?.let {
                        result.invoke(UiState.SUCCESS(it.data))

                    } ?: run {
                        result.invoke(UiState.FAILED(response.message()))
                    }
                    Log.d(TAG,"if : " )
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    Log.d(TAG,"ELSE : ${errorBodyString}" )

                    errorBodyString?.let {
                        try {
                            val errorJson = JSONObject(it)
                            val errorMessage = errorJson.getString("message")
                            Log.d(TAG,errorMessage)
                            result.invoke(UiState.FAILED(errorMessage))
                        } catch (e: JSONException) {
                            result.invoke(UiState.FAILED("Error parsing error message"))
                        }
                    } ?: run {
                        result.invoke(UiState.FAILED("Unknown error"))
                    }
                }
            }

            override fun onFailure(call: Call<ResponseData<User>>, t: Throwable) {
                result.invoke(UiState.FAILED(t.message.toString()))
                Log.d(TAG,t.toString(),t)
            }

        })
    }

    override fun signUp(
        fullname: String,
        address: String,
        phone: String,
        email: String,
        passwod: String,
        result: (UiState<ResponseData<Int>>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        authService.createUser(fullname,address,phone,email,passwod).enqueue(object  : Callback,
            retrofit2.Callback<ResponseData<Int>> {
            override fun onResponse(
                call: Call<ResponseData<Int>>,
                response: Response<ResponseData<Int>>
            ) {
                Log.d(TAG,response.body().toString())
                if (response.isSuccessful) {
                    val data : ResponseData<Int> ?= response.body()
                    Log.d(TAG,data.toString())
                    data?.let {
                        result.invoke(UiState.SUCCESS(it))
                        return@let
                    }
                    result.invoke(UiState.FAILED(response.message()))
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

            override fun onFailure(call: Call<ResponseData<Int>>, t: Throwable) {
                result.invoke(UiState.FAILED(t.message.toString()))
                Log.d(TAG,t.toString(),t)
            }
        })
    }

    override fun editProfile(
        uid: Int,
        name: String,
        address: String,
        phone: String,
        result: (UiState<ResponseData<Any>>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        authService.updateProfile(uid,name, address, phone).enqueue(object  :  Callback,
            retrofit2.Callback<ResponseData<Any>> {
            override fun onResponse(
                call: Call<ResponseData<Any>>,
                response: Response<ResponseData<Any>>
            ) {
                Log.d(TAG,response.body().toString())
                if (response.isSuccessful) {
                    val data : ResponseData<Any> ?= response.body()
                    Log.d(TAG,data.toString())
                    data?.let {
                        result.invoke(UiState.SUCCESS(it))
                        return@let
                    }
                    result.invoke(UiState.FAILED(response.message()))
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
                Log.d(TAG,t.toString(),t)
            }
        })
    }

    override fun changePassword(
        uid: Int,
        default: String,
        new: String,
        confirm: String,
        result: (UiState<ResponseData<Any>>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        authService.changePassword(uid,default, new, confirm).enqueue(object  :  Callback,
            retrofit2.Callback<ResponseData<Any>> {
            override fun onResponse(
                call: Call<ResponseData<Any>>,
                response: Response<ResponseData<Any>>
            ) {
                Log.d(TAG,response.body().toString())
                if (response.isSuccessful) {
                    val data : ResponseData<Any> ?= response.body()
                    Log.d(TAG,data.toString())
                    data?.let {
                        result.invoke(UiState.SUCCESS(it))
                        return@let
                    }
                    result.invoke(UiState.FAILED(response.message()))
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
                Log.d(TAG,t.toString(),t)
            }
        })
    }

    override suspend fun uploadProfile(
        imageUri: MultipartBody.Part,
        id : Int,
        result: (UiState<ResponseData<Any>>) -> Unit
    ) {
        result.invoke(UiState.LOADING)
        authService.uploadProfile(imageUri,id).enqueue(object : retrofit2.Callback<ResponseData<Any>> {
            override fun onResponse(
                call: Call<ResponseData<Any>>,
                response: Response<ResponseData<Any>>
            ) {
                if (response.isSuccessful) {
                    val data : ResponseData<Any> ?= response.body()
                    Log.d(TAG,data.toString())
                    data?.let {

                        result.invoke(UiState.SUCCESS(it))
                        return@let
                    }
                    result.invoke(UiState.FAILED(response.message()))
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
                Log.d(TAG,t.toString(),t)
            }

        })
    }


}


