package com.example.weather

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        myApplication = this
    }

    companion object {
        var myApplication: MyApplication?=null
        fun getMyApp() = myApplication!!
    }
}