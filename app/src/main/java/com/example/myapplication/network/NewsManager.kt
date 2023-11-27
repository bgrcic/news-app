package com.example.myapplication.network

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.myapplication.model.ArticleCategory
import com.example.myapplication.model.TopNewsResponse
import com.example.myapplication.model.getArticleCategory
import retrofit2.Call
import retrofit2.Callback

import retrofit2.Response
import retrofit2.http.Query

class NewsManager {

    private val _newsResponse = mutableStateOf(TopNewsResponse())
    val newsResponse: State<TopNewsResponse>
        @Composable get() = remember {
            _newsResponse
        }

    val sourceName = mutableStateOf("abc-news")

    private val _getArticleBySource =  mutableStateOf(TopNewsResponse())
    val getArticleBySource :MutableState<TopNewsResponse>
        @Composable get() = remember {
            _getArticleBySource
        }

    private val _getArticleByCategory =
        mutableStateOf(TopNewsResponse())
    val getArticleByCategory: MutableState<TopNewsResponse>
        @Composable get() = remember {
            _getArticleByCategory
        }

    val query = mutableStateOf("")

    val _searchedNewsResponse = mutableStateOf(TopNewsResponse())
    val searchedNewsResponse : MutableState<TopNewsResponse>
        @Composable get() = remember {
            _searchedNewsResponse
        }

    val selectedCategory: MutableState<ArticleCategory?> = mutableStateOf(null)

    init {
        getArticles()
    }

    private fun getArticles() {
        val service = Api.retrofitService.getTopArticles("us")
        service.enqueue(object : Callback<TopNewsResponse> {
            override fun onResponse(
                call: Call<TopNewsResponse>,
                response: Response<TopNewsResponse>
            ) {
                if (response.isSuccessful) {
                    _newsResponse.value = response.body()!!
                    Log.d("news", "${_newsResponse.value}")
                } else {
                    Log.d("error", "${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<TopNewsResponse>, t: Throwable) {
                Log.d("error", "${t.printStackTrace()}")
            }
        })
    }

    fun getArticlesByCategory(category: String) {
        val service = Api.retrofitService.getArticlesByCategory(category)
        service.enqueue(object : Callback<TopNewsResponse> {
            override fun onResponse(
                call: Call<TopNewsResponse>,
                response: Response<TopNewsResponse>
            ) {
                if (response.isSuccessful) {
                    _getArticleByCategory.value = response.body()!!
                    Log.d("news", "${_getArticleByCategory.value}")
                } else {
                    Log.d("error", "${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<TopNewsResponse>, t: Throwable) {
                Log.d("error", "${t.printStackTrace()}")
            }
        })
    }

    fun getArticlesBySource() {
        val service = Api.retrofitService.getArticlesBySouurces(sourceName.value)
        service.enqueue(object : Callback<TopNewsResponse> {
            override fun onResponse(
                call: Call<TopNewsResponse>,
                response: Response<TopNewsResponse>
            ) {
                if (response.isSuccessful) {
                    _getArticleBySource.value = response.body()!!
                    Log.d("news", "${_getArticleBySource.value}")
                } else {
                    Log.d("error", "${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<TopNewsResponse>, t: Throwable) {
                Log.d("error", "${t.printStackTrace()}")
            }
        })
    }

    fun getSearchedArticles(query: String) {
        val service = Api.retrofitService.getArticles(query)
        service.enqueue(object : Callback<TopNewsResponse> {
            override fun onResponse(
                call: Call<TopNewsResponse>,
                response: Response<TopNewsResponse>
            ) {
                if (response.isSuccessful) {
                    _searchedNewsResponse.value = response.body()!!
                    Log.d("search", "${_searchedNewsResponse.value}")
                } else {
                    Log.d("search", "${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<TopNewsResponse>, t: Throwable) {
                Log.d("searcherror", "${t.printStackTrace()}")
            }
        })
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getArticleCategory(category = category)
        selectedCategory.value = newCategory
    }
}