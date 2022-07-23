package com.example.weather.viewmodel.citieslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.repositories.RepoCitiesListImpl
import com.example.weather.interfaces.RepositoryCitiesList
import com.example.weather.utils.Location

class CitiesListViewModel(private val lifeData: MutableLiveData<CityListFragmentAppState> = MutableLiveData<CityListFragmentAppState>()) :
    ViewModel() {

    private lateinit var repository: RepositoryCitiesList

    fun getLiveData(): MutableLiveData<CityListFragmentAppState> {
        return lifeData
    }

    private fun choiceRepo() {
        repository = RepoCitiesListImpl()
    }

    fun getWeatherForRussia() {
        sendRequest(Location.Russian)
    }

    fun getWeatherForWorld() {
        sendRequest(Location.World)
    }

    private fun sendRequest(location: Location) {
        choiceRepo()
        lifeData.value = CityListFragmentAppState.Loading
        lifeData.postValue(
            CityListFragmentAppState.SuccessForManyLocations(
                repository.getCitiesList(location)
            )
        )
    }
}


