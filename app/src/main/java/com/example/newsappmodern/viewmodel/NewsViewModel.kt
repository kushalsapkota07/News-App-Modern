package com.example.newsappmodern.viewmodel

import android.app.Application
import android.content.Context
import android.support.v4.os.IResultReceiver2._Parcel
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsappmodern.models.Article
import com.example.newsappmodern.models.NewsResponse
import com.example.newsappmodern.models.UserPrefs
import com.example.newsappmodern.repository.NewsRepository
import com.example.newsappmodern.repository.UserPreferencesRepository
import com.example.newsappmodern.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    app:Application,
    private val newsRepository: NewsRepository
): AndroidViewModel(app) {

    val isConfirmed: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    fun confirm(confirm:Boolean){
        isConfirmed.value = confirm
    }

    // A map to hold 'LiveData' objects for each category
    private val _categoryNewsMap: MutableMap<String, MutableLiveData<Resource<NewsResponse>>> = mutableMapOf()

    var allNewsResponse: MutableMap<String,NewsResponse> = mutableMapOf()
    var currentNewsPage:String? = null

    private val _latestNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val latestNews: LiveData<Resource<NewsResponse>> = _latestNews
    var latestNewsResponse:NewsResponse ? = null
    var latestNewsPage:String? = null

    private val _topTrendingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val topTrendingNews: LiveData<Resource<NewsResponse>> = _topTrendingNews
    var topTrendingNewsResponse:NewsResponse ? = null
    var topTrendingNewsPage:String? = null

    val selectedCategory = MutableLiveData<String>()

    var topTrendingPage = 1
    var currentNewsCategoryPage = 1

//    init {
//        getTopTrendingNews("wo","top")
//    }

    fun getCurrentNews(countryCode: String,category: String): LiveData<Resource<NewsResponse>>{
        return _categoryNewsMap.getOrPut(category){
            MutableLiveData<Resource<NewsResponse>>()
                .apply {
                    postValue(Resource.Loading())
                }
                .also {
                loadDataForCategory(countryCode,category) //Trigger Data load when LiveData is created
            }
        }
    }

    fun getLatestNews(countryCode: String, category: String?) {
        viewModelScope.launch {
            _latestNews.postValue(Resource.Loading())
            val response: Response<NewsResponse> = newsRepository.getLatestNews(countryCode,category,latestNewsPage)
            _latestNews.postValue(handleLatestNewsResponse(response))
        }
    }

     fun getTopTrendingNews(countryCode: String,category: String){
        viewModelScope.launch {
            _topTrendingNews.postValue(Resource.Loading())
            val response: Response<NewsResponse> = newsRepository.getTopTrendingNews(countryCode,category,topTrendingNewsPage)
            _topTrendingNews.postValue(handleTopTrendingNewsResponse(response))
        }
    }

    //Loading data and posting it to the specific LiveData entry in the map

    private fun loadDataForCategory( countryCode: String,category: String){
        viewModelScope.launch {
            val response: Response<NewsResponse> = newsRepository.getLatestNews(countryCode, category,currentNewsPage)
            _categoryNewsMap[category]?.postValue(handleCurrentNewsResponse(response,category))
        }
    }
    private fun handleCurrentNewsResponse(response: Response<NewsResponse>, category: String): Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                if(allNewsResponse[category]==null){
                    allNewsResponse[category] = resultResponse
                } else{
                    val oldArticles = allNewsResponse[category]?.results
                    val newArticles = resultResponse.results
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(allNewsResponse[category]?: resultResponse)
            }
        }
        return Resource.Error(response.message())
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

    private fun handleTopTrendingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                if(topTrendingNewsResponse==null){
                    topTrendingNewsResponse = resultResponse
                } else{
                    val oldArticles = topTrendingNewsResponse?.results
                    val newArticles = resultResponse.results
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(topTrendingNewsResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article){
        viewModelScope.launch {
            newsRepository.insert(article)
        }
    }

    fun getSavedNews(): LiveData<List<Article>>{
        return newsRepository.getSavedNews()
    }

    fun deleteArticle(article: Article){
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
        }
    }
}