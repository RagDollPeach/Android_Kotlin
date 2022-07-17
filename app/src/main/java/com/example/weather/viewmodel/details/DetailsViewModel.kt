package com.example.weather.viewmodel.details

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.MyApplication
import com.example.weather.domain.Weather
import com.example.weather.model.*
import com.example.weather.model.retrofit.RepoDetailsRetrofitImpl
import java.io.IOException


class DetailsViewModel(private val lifeData: MutableLiveData<DetailsFragmentAppState> = MutableLiveData<DetailsFragmentAppState>()) :
    ViewModel() {

    private lateinit var repository: RepositoryDetails
    private lateinit var repositoryInsertable: RepositoryRoomInsertable

    fun getLiveData(): MutableLiveData<DetailsFragmentAppState> {
        return lifeData
    }

    private fun choiceRepo() {
        val pref =
            MyApplication.getMyApp().getSharedPreferences("radio_buttons", Context.MODE_PRIVATE)
        repository = if (isConnection(MyApplication.getMyApp())) {
            when (pref.getInt("pref", 0)) {
                1 -> { RepoDetailsOkHttpImpl() }
                2 -> { RepoDetailsRetrofitImpl() }
                3 -> { RepositoryDetailsWeatherLoaderImpl() }
                4 -> { RepositoryRoomImpl() }
                else -> { RepoDetailsLocalImpl() }
            }
        } else {
            when (pref.getInt("pref", 0)) {
                4 -> { RepositoryRoomImpl() }
                else -> { RepoDetailsLocalImpl() }
            }
        }

        when(repository.javaClass) {
            RepoDetailsOkHttpImpl().javaClass -> Toast.makeText(MyApplication.getMyApp(),"OkHTTP Working",Toast.LENGTH_SHORT).show()
            RepoDetailsRetrofitImpl().javaClass -> Toast.makeText(MyApplication.getMyApp(),"Retrofit Working",Toast.LENGTH_SHORT).show()
            RepositoryDetailsWeatherLoaderImpl().javaClass -> Toast.makeText(MyApplication.getMyApp(),"WeatherLoader Working",Toast.LENGTH_SHORT).show()
            RepoDetailsLocalImpl().javaClass -> Toast.makeText(MyApplication.getMyApp(),"Local Working",Toast.LENGTH_SHORT).show()
            RepositoryRoomImpl().javaClass -> Toast.makeText(MyApplication.getMyApp(),"Room Working",Toast.LENGTH_SHORT).show()
        }

        repositoryInsertable = RepositoryRoomImpl()
    }

    fun getWeather(weather: Weather) {
        choiceRepo()
        lifeData.value = DetailsFragmentAppState.Loading
        repository.getWeather(weather, callBack)
    }

    private val callBack = object : MyLargeFatCallBack {
        override fun onResponse(weather: Weather) {
            if (isConnection(MyApplication.getMyApp())) {
                repositoryInsertable.insertWeather(weather)
            }
            lifeData.postValue(DetailsFragmentAppState.Success(weather))
        }

        override fun onError(e: IOException) {
            lifeData.postValue(DetailsFragmentAppState.Error(e))
        }
    }

    private fun isConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (wifiInfo != null && wifiInfo.isConnected) {
            return true
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (wifiInfo != null && wifiInfo.isConnected) {
            return true
        }
        wifiInfo = cm.activeNetworkInfo
        return wifiInfo != null && wifiInfo.isConnected
    }
}


