package com.example.weather.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weather.domain.Weather
import com.example.weather.model.dto.Fact
import com.example.weather.model.dto.WeatherDTO
import java.io.BufferedReader
import java.util.stream.Collectors

@RequiresApi(Build.VERSION_CODES.N)
fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining())
}

fun convertDtoToModel(weatherDTO: WeatherDTO,weather: Weather): Weather {
    val fact: Fact = weatherDTO.fact
    return Weather(weather.city, fact.temp, fact.feelsLike, fact.icon)
}

fun convertModelToDto(weather: Weather): WeatherDTO {
    val fact = Fact(weather.feelsLike,weather.temperature, weather.icon)
    return WeatherDTO(fact)
}
