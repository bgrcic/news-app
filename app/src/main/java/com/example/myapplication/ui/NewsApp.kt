@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication.ui

import DetailsScreen
import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.BottomMenuScreen
import com.example.myapplication.MockData
import com.example.myapplication.components.BottomMenu
import com.example.myapplication.ui.screen.Categories
import com.example.myapplication.ui.screen.Sources
import com.example.myapplication.ui.screen.TopNews

@Composable
fun NewsApp() {
    val scrollState = rememberScrollState()
    val navController = rememberNavController()
    MainScreen(navController, scrollState)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, scrollState: ScrollState) {
    Scaffold(
        bottomBar = {
            BottomMenu(navController = navController)
        },
    ) {
        Navigation(navController = navController, scrollState = scrollState)
    }
}

@Composable
fun Navigation(navController: NavHostController, scrollState: ScrollState) {
    NavHost(navController = navController, startDestination = "TopNews") {
        bottomNavigation(navController = navController)
        composable("TopNews") {
            TopNews(navController = navController)
        }
        composable("Details/{newsId}",
            arguments = listOf(
                navArgument("newsId") { type = NavType.IntType }
            )) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("newsId")
            val newsData = MockData.getNews(id)
            DetailsScreen(newsData, scrollState, navController)
        }
    }
}

fun NavGraphBuilder.bottomNavigation(navController: NavController) {
    composable(BottomMenuScreen.TopNews.route) {
        TopNews(navController = navController)
    }
    composable(BottomMenuScreen.TopNews.route) {
        Categories()
    }
    composable(BottomMenuScreen.TopNews.route) {
        Sources()
    }
}