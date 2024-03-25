package com.danica.msbapb.services

import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.News
import com.danica.msbapb.models.Personels
import retrofit2.Call
import retrofit2.http.GET

interface NewsService {
    @GET("api/news/get_all_news.php")
    fun getAllNews(): Call<ResponseData<List<News>>>
}