package com.danica.msbapb.repository.perosnels

import com.danica.msbapb.models.Personels
import com.danica.msbapb.utils.UiState

interface PersonelRepository {
  suspend fun getAllPersonels(result : (UiState<List<Personels>>) -> Unit)
}