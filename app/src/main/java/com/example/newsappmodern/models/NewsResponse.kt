package com.example.newsappmodern.models

data class NewsResponse(
    val status:String,
    val totalResults: Int,
    val results: MutableList<Article>,
    val nextPage:String?
)
