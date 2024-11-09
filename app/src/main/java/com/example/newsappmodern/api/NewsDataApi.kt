package com.example.newsappmodern.api

import com.example.newsappmodern.models.NewsResponse
import com.example.newsappmodern.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsDataApi {

    @GET("1/latest")
    suspend fun getLatestNews(
        @Query("country")
        countryCode:String = "au",
        @Query("page")
        page:String?=null,
        @Query("category")
        category: String?="entertainment",
        @Query("apiKey")
        apiKey:String= API_KEY
    ): Response<NewsResponse>

    @GET("1/sources")
    suspend fun searchNews(
        @Query("country")
        countryCode: String,
        @Query("q")
        query:String,
        @Query("category")
        category: String,
        @Query("page")
        page:String,
        @Query("apiKey")
        apiKey: String = API_KEY
    )

}

//https://newsdata.io/api/1/latest?apikey=pub_57534a151e9b364446a690336854c5aecf683
//https://newsdata.io/api/1/sources?apikey=pub_57534a151e9b364446a690336854c5aecf683&category=politics,world