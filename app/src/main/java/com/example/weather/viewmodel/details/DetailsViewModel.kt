package com.example.weather.viewmodel.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.*
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.model.retrofit.RepoDetailsRetrofitImpl
import com.example.weather.view.weatherlist.CitiesListFragment
import java.io.IOException

class DetailsViewModel(private val lifeData: MutableLiveData<DetailsFragmentAppState> = MutableLiveData<DetailsFragmentAppState>()) :
    ViewModel() {

    private lateinit var repository: RepositoryDetails


    fun getLiveData(): MutableLiveData<DetailsFragmentAppState> {
        return lifeData
    }

    private fun choiceRepo() {
        repository = when (CitiesListFragment.list[0]) {
            1 -> { RepoDetailsOkHttpImpl() }
            2 -> { RepoDetailsRetrofitImpl() }
            3 -> { RepositoryDetailsWeatherLoaderImpl() }
            else -> { RepoDetailsLocalImpl() }
        }
    }

    fun getWeather(lat: Double, lon: Double) {
        choiceRepo()
        lifeData.value = DetailsFragmentAppState.Loading
        repository.getWeather(lat, lon, callBack)
    }

    private val callBack = object : MyLargeFatCallBack {
        override fun onResponse(weatherDTO: WeatherDTO) {
            lifeData.postValue(DetailsFragmentAppState.Success(weatherDTO))
        }

        override fun onError(e: IOException) {
            lifeData.postValue(DetailsFragmentAppState.Error(e))
        }
    }

    private fun isConnection(): Boolean {
        return false
    }
}


