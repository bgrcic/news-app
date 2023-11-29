package com.example.myapplication.data

import com.example.myapplication.network.NewsManager

class Repository(val manager: NewsManager) {
    suspend fun getArticles() = manager.getArticles("us")
    suspend fun getArticleByCategory(category: String) = manager.getArticlesByCategory(category)

    suspend fun getArticlesBySource(source: String) = manager.getArticlesBySource(source = source)

    suspend fun getSearchedArticles(query: String) = manager.getSearchedArticles(query = query)
}