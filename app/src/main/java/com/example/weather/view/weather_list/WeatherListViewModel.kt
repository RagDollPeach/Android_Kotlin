package com.example.weather.view.weather_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.RepoLocalImpl
import com.example.weather.model.RepoRemoteImpl
import com.example.weather.model.RepositoryForManyLocations
import com.example.weather.model.RepositoryForOneLocation
import com.example.weather.utils.Location
import com.example.weather.view_model.AppState

class WeatherListViewModel(private val lifeData: MutableLiveData<AppState> = MutableLiveData<AppState>()) :
    ViewModel() {

    private lateinit var repositoryForManyLocations: RepositoryForManyLocations
    private lateinit var repositoryForOneLocation: RepositoryForOneLocation

    fun getLiveData(): MutableLiveData<AppState> {
        return lifeData
    }

    private fun choiceRepo() {
        repositoryForOneLocation = if (isConnection()) {
            RepoRemoteImpl()
        } else {
            RepoLocalImpl()
        }
        repositoryForManyLocations = RepoLocalImpl()
    }

    fun getWeatherForRussia() {
        sendRequest(Location.Russian)
    }

    fun getWeatherForWorld() {
        sendRequest(Location.World)
    }

    private fun sendRequest(location: Location) {
        choiceRepo()
        lifeData.value = AppState.Loading
        lifeData.postValue(AppState.SuccessForManyLocations(
                repositoryForManyLocations.getWeatherList(location)))
    }


    private fun isConnection(): Boolean {
        return false
    }
}


