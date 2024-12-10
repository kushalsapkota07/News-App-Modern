package com.example.newsappmodern.repository

import androidx.lifecycle.LiveData
import com.example.newsappmodern.NewsApplication
import com.example.newsappmodern.api.RetrofitInstance
import com.example.newsappmodern.db.ArticleDatabase
import com.example.newsappmodern.models.Article
import com.example.newsappmodern.models.NewsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NewsRepository(
    private val db: ArticleDatabase
) {

    suspend fun getLatestNews(
        countryCode: String,
        category: String?,
        pageNumber:String? = null
    ): Response<NewsResponse>{
        return RetrofitInstance.api.getLatestNews(
            countryCode,
            category,
            pageNumber
        )
    }

    suspend fun getTopTrendingNews(
        countryCode: String?=null,
        category: String,
        pageNumber:String? = null
    ): Response<NewsResponse>{
        return RetrofitInstance.api.getTopTrendingNews(
            countryCode,
            category,
            pageNumber
        )
    }

    suspend fun insert(article: Article): Long{
        return db.getArticleDao().insert(article)
    }

    fun getSavedNews() : LiveData<List<Article>>{
        return db.getArticleDao().getAllArticles()
    }

    suspend fun deleteArticle(article: Article){
        db.getArticleDao().deleteArticle(article)
    }
}