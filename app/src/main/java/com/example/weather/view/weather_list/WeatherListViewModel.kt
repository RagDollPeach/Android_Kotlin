package com.example.weather.view.weather_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.RepoLocalImpl
import com.example.weather.model.RepoRemoteImpl
import com.example.weather.model.Repository
import com.example.weather.view_model.AppState
import java.lang.Thread.sleep

class WeatherListViewModel(
    private val lifeData: MutableLiveData<AppState> = MutableLiveData<AppState>()
) : ViewModel() {

    private lateinit var repository: Repository

    fun getLiveData(): MutableLiveData<AppState> {
        return lifeData
    }

    private fun choiceRepo() {
        repository = if (isConnection()) {
            RepoRemoteImpl()
        } else {
            RepoLocalImpl()
        }
    }

    fun sendRequest() {
        choiceRepo()
        lifeData.value = AppState.Loading
        if ((0..3).random() == 1) {
            lifeData.postValue(AppState.Error(throw IllegalStateException("Ошибка соединения или фиг ее знает")))
        } else {
            lifeData.postValue(AppState.Success(repository.getWeather(55.755826, 37.617299900000035)))
        }
    }

    private fun isConnection(): Boolean {
        return false
    }
}

