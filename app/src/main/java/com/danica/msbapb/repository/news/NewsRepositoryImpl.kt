package com.danica.msbapb.repository.news

import android.util.Log
import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.News
import com.danica.msbapb.repository.perosnels.PERSONEL_TAG
import com.danica.msbapb.services.NewsService
import com.danica.msbapb.utils.UiState
import retrofit2.Call

import retrofit2.Response
import javax.security.auth.callback.Callback
const val NEWS_TAG = "news"
class NewsRepositoryImpl(private val newsService: NewsService): NewsRepository {
    override suspend fun getAllNews(result: (UiState<ResponseData<List<News>>>) -> Unit) {
        result.invoke(UiState.LOADING)
        newsService.getAllNews().enqueue(object : Callback, retrofit2.Callback<ResponseData<List<News>>>{
            override fun onResponse(
                call: Call<ResponseData<List<News>>>,
                response: Response<ResponseData<List<News>>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body() ?: ResponseData(false,"unknown error", emptyList())
                    Log.d(NEWS_TAG,data.toString())
                    result.invoke(UiState.SUCCESS(data))

                } else {
                    Log.d(NEWS_TAG,response.errorBody().toString())
                    result.invoke(UiState.FAILED(response.errorBody().toString()))
                }
            }
            override fun onFailure(call: Call<ResponseData<List<News>>>, t: Throwable) {
                Log.d(NEWS_TAG,t.message.toString())
                result.invoke(UiState.FAILED(t.message.toString()))
            }
        })
    }
}