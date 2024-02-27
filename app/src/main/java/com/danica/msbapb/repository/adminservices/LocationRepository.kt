package com.danica.msbapb.repository.adminservices

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.Locations
import com.danica.msbapb.utils.UiState

interface LocationRepository {
   suspend fun  getAllLocations(result : (UiState<ResponseData<List<Locations>>>) -> Unit)
}