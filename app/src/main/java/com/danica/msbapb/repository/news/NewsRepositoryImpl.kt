package com.danica.msbapb.repository.news

import android.util.Log
import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.News
import com.danica.msbapb.repository.perosnels.PERSONEL_TAG
import com.danica.msbapb.services.NewsService
import com.danica.msbapb.utils.UiState
import org.json.JSONException
import org.json.JSONObject
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
            override fun onFailure(call: Call<ResponseData<List<News>>>, t: Throwable) {
                Log.d(NEWS_TAG,t.message.toString())
                result.invoke(UiState.FAILED(t.message.toString()))
            }
        })
    }
}