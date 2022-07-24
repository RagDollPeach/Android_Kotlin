package com.example.weather.viewmodel.details

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.MyApplication
import com.example.weather.domain.Weather
import com.example.weather.interfaces.MyLargeFatCallBack
import com.example.weather.interfaces.RepositoryDetails
import com.example.weather.interfaces.RepositoryRoomInsertable
import com.example.weather.model.retrofit.RepoDetailsRetrofitImpl
import com.example.weather.model.repositories.RepoDetailsLocalImpl
import com.example.weather.model.repositories.RepoDetailsOkHttpImpl
import com.example.weather.model.repositories.RepositoryDetailsWeatherLoaderImpl
import com.example.weather.model.repositories.RepositoryRoomImpl


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
        val response = MyApplication.getMyApp().getSharedPreferences("repo_check", Context.MODE_PRIVATE)
        when(repository.javaClass) {
            RepoDetailsOkHttpImpl().javaClass -> response.edit().putString("resp", "OkHTTP Working").apply()
            RepoDetailsRetrofitImpl().javaClass -> response.edit().putString("resp", "Retrofit Working").apply()
            RepositoryDetailsWeatherLoaderImpl().javaClass -> response.edit().putString("resp","WeatherLoader Working").apply()
            RepoDetailsLocalImpl().javaClass -> response.edit().putString("resp","Local Working").apply()
            RepositoryRoomImpl().javaClass -> response.edit().putString("resp","Room Working").apply()
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
            Thread {
                if (isConnection(MyApplication.getMyApp())) {
                    repositoryInsertable.insertWeather(weather)
                }
            }.start()
            lifeData.postValue(DetailsFragmentAppState.Success(weather))
        }

        override fun onError(e: Exception) {
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


