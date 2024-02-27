package com.danica.msbapb.repository.perosnels

import android.util.Log
import com.danica.msbapb.models.Personels
import com.danica.msbapb.repository.incidents.INCIDENT_TAG
import com.danica.msbapb.services.PersonelService
import com.danica.msbapb.utils.UiState
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
const val  PERSONEL_TAG = "personels"
class PersonelRepositoryImpl(private val  personelService: PersonelService): PersonelRepository {
    override suspend fun getAllPersonels(result : (UiState<List<Personels>>) -> Unit) {
        result.invoke(UiState.LOADING)
        personelService.getAllPersonels().enqueue(object  :Callback,
            retrofit2.Callback<List<Personels>> {
            override fun onResponse(
                call: Call<List<Personels>>,
                response: Response<List<Personels>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    Log.d(PERSONEL_TAG,data.toString())
                    result.invoke(UiState.SUCCESS(data))

                } else {
                    Log.d(PERSONEL_TAG,response.errorBody().toString())
                    result.invoke(UiState.FAILED(response.errorBody().toString()))
                }
            }

            override fun onFailure(call: Call<List<Personels>>, t: Throwable) {
                Log.d(PERSONEL_TAG,t.message.toString())
                result.invoke(UiState.FAILED(t.message.toString()))
            }
        })
    }
}