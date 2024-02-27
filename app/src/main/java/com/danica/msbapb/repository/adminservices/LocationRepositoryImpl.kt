package com.danica.msbapb.repository.adminservices


import android.util.Log
import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.Locations
import com.danica.msbapb.models.Personels
import com.danica.msbapb.repository.perosnels.PERSONEL_TAG

import com.danica.msbapb.services.LocationService
import com.danica.msbapb.utils.UiState
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


const val LOCATION_TAG = "locations"

class LocationRepositoryImpl(private val locationService: LocationService): LocationRepository {
    override suspend fun getAllLocations(result: (UiState<ResponseData<List<Locations>>>) -> Unit) {
        result.invoke(UiState.LOADING)
        locationService.getAllLocations().enqueue(object  :Callback,
            retrofit2.Callback<ResponseData<List<Locations>>> {
            override fun onResponse(
                call: Call<ResponseData<List<Locations>>>,
                response: Response<ResponseData<List<Locations>>>
            ) {
                if (response.isSuccessful) {
                    val data : ResponseData<List<Locations>> = response.body() ?: ResponseData(false ,"Unknown Error",
                        emptyList()
                    )
                    Log.d(LOCATION_TAG,data.toString())
                    result.invoke(UiState.SUCCESS(data))

                } else {
                    Log.d(LOCATION_TAG,response.errorBody().toString())
                    result.invoke(UiState.FAILED(response.errorBody().toString()))
                }
            }

            override fun onFailure(call: Call<ResponseData<List<Locations>>>, t: Throwable) {
                Log.d(LOCATION_TAG,t.message.toString())
                result.invoke(UiState.FAILED(t.message.toString()))
            }
        })
    }
}