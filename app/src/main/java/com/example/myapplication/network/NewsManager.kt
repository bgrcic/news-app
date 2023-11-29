package com.example.myapplication.network

import com.example.myapplication.model.TopNewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsManager(private val service: NewsService) {

    suspend fun getArticles(country: String): TopNewsResponse = withContext(Dispatchers.IO) {
        service.getTopArticles(country = country)
    }

    suspend fun getArticlesByCategory(category: String): TopNewsResponse =
        withContext(Dispatchers.IO) {
            service.getArticlesByCategory(category = category)
        }

    suspend fun getArticlesBySource(source: String): TopNewsResponse = withContext(Dispatchers.IO) {
        service.getArticlesBySouurces(source)
    }

    suspend fun getSearchedArticles(query: String): TopNewsResponse = withContext(Dispatchers.IO) {
        service.getArticles(query)
    }
}