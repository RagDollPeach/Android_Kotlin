package com.example.weather.viewmodel.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.*
import com.example.weather.utils.Location
import com.example.weather.viewmodel.citieslist.CityListFragmentAppState

class DetailsViewModel(private val lifeData: MutableLiveData<DetailsFragmentAppState> = MutableLiveData<DetailsFragmentAppState>()) :
    ViewModel() {

    private lateinit var repository: RepositoryDetails


    fun getLiveData(): MutableLiveData<DetailsFragmentAppState> {
        return lifeData
    }

    private fun choiceRepo() {
        repository = when(1) {
            1 -> { RepoDetailsOkHttpImpl() }
            2 -> { RepoDetailsRetrofitImpl() }
            3 -> { RepositoryDetailsWeatherLoaderImpl() }
            else -> { RepoDetailsLocalImpl() }
        }
    }

    private fun getWeather(lat: Double, lon: Double) {
        choiceRepo()
        lifeData.value = DetailsFragmentAppState.Loading
        lifeData.postValue(DetailsFragmentAppState.Success(repository.getWeather(lat, lon)))
    }

    private fun isConnection(): Boolean {
        return false
    }
}


