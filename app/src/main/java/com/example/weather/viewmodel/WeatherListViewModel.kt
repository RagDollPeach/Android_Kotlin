package com.example.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.RepoLocalImpl
import com.example.weather.model.RepoRemoteImpl
import com.example.weather.model.RepositoryForManyLocations
import com.example.weather.model.RepositoryForOneLocation
import com.example.weather.utils.Location
import com.example.weather.viewmodel.AppState
import com.google.android.material.snackbar.Snackbar

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
        Thread {
            Thread.sleep(50)
            if ((0..10).random() == 5) {
                lifeData.postValue(AppState.Error(IllegalStateException("не нужная часть кода")))
            } else {
                lifeData.postValue(AppState
                    .SuccessForManyLocations(repositoryForManyLocations.getWeatherList(location)))
            }
        }.start()
    }

    private fun isConnection(): Boolean {
        return false
    }
}


