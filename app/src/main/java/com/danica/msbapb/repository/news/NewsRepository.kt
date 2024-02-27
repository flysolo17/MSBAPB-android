package com.danica.msbapb.repository.news

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.News
import com.danica.msbapb.utils.UiState

interface NewsRepository {
    suspend fun getAllNews(result : (UiState<ResponseData<List<News>>>) -> Unit)
}