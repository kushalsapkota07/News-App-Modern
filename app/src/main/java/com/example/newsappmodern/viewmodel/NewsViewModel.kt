package com.example.newsappmodern.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsappmodern.api.RetrofitInstance
import com.example.newsappmodern.models.NewsResponse
import com.example.newsappmodern.repository.NewsRepository
import com.example.newsappmodern.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    app:Application,
    private val newsRepository: NewsRepository
): AndroidViewModel(app) {

    private val _latestNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val latestNews: LiveData<Resource<NewsResponse>> = _latestNews
    var latestNewsResponse:NewsResponse ? = null
    var latestNewsPage:String? = null

    init {
        getLatestNews("au")
    }

    private fun getLatestNews(countryCode: String) {
        viewModelScope.launch {
            _latestNews.postValue(Resource.Loading())
            val response: Response<NewsResponse> = newsRepository.getLatestNews(countryCode,latestNewsPage)
            _latestNews.postValue(handleLatestNewsResponse(response))

        }
    }

    private fun handleLatestNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                if(latestNewsResponse==null){
                    latestNewsResponse = resultResponse
                } else{
                    val oldArticles = latestNewsResponse?.results
                    val newArticles = resultResponse.results
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(latestNewsResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}