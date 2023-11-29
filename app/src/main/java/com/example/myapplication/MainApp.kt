package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.Repository
import com.example.myapplication.network.Api
import com.example.myapplication.network.NewsManager

class MainApp: Application() {

    private val manager by lazy{
        NewsManager(Api.retrofitService)
    }

    val repository by lazy{
       Repository(manager = manager)
    }
}