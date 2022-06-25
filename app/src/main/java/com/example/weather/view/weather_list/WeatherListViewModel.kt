package com.example.weather.view.weather_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.view_model.AppState
import java.lang.Thread.sleep

class WeatherListViewModel(val lifeData: MutableLiveData<AppState> = MutableLiveData<AppState>()) : ViewModel() {

    fun sendRequest() {
        lifeData.value = AppState.Loading
        Thread{
            sleep(2000)
            lifeData.postValue(AppState.Success(Any()))
        }.start()

    }
}

