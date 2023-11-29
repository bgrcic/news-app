@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication.ui

import DetailsScreen
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.BottomMenuScreen
import com.example.myapplication.components.BottomMenu
import com.example.myapplication.model.TopNewsArticle
import com.example.myapplication.ui.screen.Categories
import com.example.myapplication.ui.screen.Sources
import com.example.myapplication.ui.screen.TopNews

@Composable
fun NewsApp(mainViewModel: MainViewModel) {
    val scrollState = rememberScrollState()
    val navController = rememberNavController()
    MainScreen(navController, scrollState, mainViewModel = mainViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController,
    scrollState: ScrollState,
    mainViewModel: MainViewModel
) {
    Scaffold(
        bottomBar = {
            BottomMenu(navController = navController)
        },
    ) {
        Navigation(
            navController = navController,
            scrollState = scrollState,
            paddingValues = it,
            viewModel = mainViewModel
        )
    }
}

@Composable
fun Navigation(
    navController: NavHostController, scrollState: ScrollState,
    paddingValues: PaddingValues,
    viewModel: MainViewModel
) {
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.isErorr.collectAsState()
    val articles = mutableListOf(TopNewsArticle())
    val topArticles = viewModel.newsResponse.collectAsState().value.articles
    articles.addAll(topArticles ?: listOf())

    Log.d("news", "$articles")

    NavHost(
        navController = navController,
        startDestination = BottomMenuScreen.TopNews.route,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        val queryState = mutableStateOf(viewModel.query.value)
        val isLoading = mutableStateOf(loading)
        val isError = mutableStateOf(error)
        bottomNavigation(
            navController = navController,
            articles,
            viewModel = viewModel,
            query = queryState,
            isError = isError,
            isLoading = isLoading
        )
        composable("Details/{index}",
            arguments = listOf(
                navArgument("index") { type = NavType.IntType }
            )) { navBackStackEntry ->
            val index = navBackStackEntry.arguments?.getInt("index")
            index?.let {
                if (queryState.value.isNotEmpty()) {
                    articles.clear()
                    articles.addAll(viewModel.searchedNewsResponse.value.articles ?: listOf())
                } else {
                    articles.clear()
                    articles.addAll(viewModel.newsResponse.value.articles ?: listOf())
                }
                val article = articles[index]
                DetailsScreen(article, scrollState, navController)
            }
        }
    }

}

fun NavGraphBuilder.bottomNavigation(
    navController: NavController, articles: List<TopNewsArticle>,
    query: MutableState<String>,
    viewModel: MainViewModel,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>
) {
    composable(BottomMenuScreen.TopNews.route) {
        TopNews(
            navController = navController,
            articles,
            query,
            viewModel = viewModel,
            isLoading = isLoading,
            isError = isError
        )
    }
    composable(BottomMenuScreen.Categories.route) {

        viewModel.getArticlesByCategory("business")
        viewModel.onSelectedCategoryChanged("business")

        Categories(
            viewModel = viewModel,
            onFetchCategory = {
                viewModel.onSelectedCategoryChanged(it)
                viewModel.getArticlesByCategory(it)
            },
            isLoading = isLoading, isError = isError,
        )
    }
    composable(BottomMenuScreen.Sources.route) {
        Sources(viewModel = viewModel, isLoading = isLoading, isError = isError)
    }
}