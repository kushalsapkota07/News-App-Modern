package com.example.newsappmodern.repository

import com.example.newsappmodern.api.RetrofitInstance
import com.example.newsappmodern.models.NewsResponse
import retrofit2.Response

class NewsRepository {

    suspend fun getLatestNews(
        countryCode: String,
        pageNumber:String? = null
    ): Response<NewsResponse>{
        return RetrofitInstance.api.getLatestNews(
            countryCode,
            pageNumber
        )
    }
}