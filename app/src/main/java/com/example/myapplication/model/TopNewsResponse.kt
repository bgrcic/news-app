package com.example.myapplication.model

data class TopNewsResponse(
    val status: String? = null,
    val totalResults: String? = null,
    val articles: List<TopNewsArticle>? = null

)
