package com.danica.msbapb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danica.msbapb.data.ResponseData
import com.danica.msbapb.models.Locations
import com.danica.msbapb.models.News
import com.danica.msbapb.repository.news.NewsRepository
import com.danica.msbapb.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository): ViewModel() {

    private val _news = MutableLiveData<UiState<ResponseData<List<News>>>>()
    val news : LiveData<UiState<ResponseData<List<News>>>> get() = _news
    fun getAllNews() {
        viewModelScope.launch {
            newsRepository.getAllNews {
                _news.value = it
            }
        }
    }

}