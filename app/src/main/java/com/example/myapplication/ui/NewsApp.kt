package com.example.myapplication.ui

import DetailsScreen
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.MockData
import com.example.myapplication.ui.screen.TopNews

@Composable
fun NewsApp() {
    Navigation()
}

@Composable
fun Navigation() {
    //Todo 7: create scrollState remember value
    val scrollState = rememberScrollState()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "TopNews") {
        composable("TopNews") {
            TopNews(navController = navController)
        }
        composable("Details/{newsId}",
            arguments = listOf(
                navArgument("newsId") { type = NavType.IntType }
            )) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("newsId")
            val newsData = MockData.getNews(id)
            //Todo 8: pass in scroll state value
            DetailsScreen(newsData, scrollState,navController)
        }
    }
}