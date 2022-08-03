package com.example.weather

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weather.interfaces.WeatherApi
import com.example.weather.model.room.WeatherDatabase
import com.example.weather.utils.ROOM_DATABASE
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        myApplication = this
    }

    companion object {
        private var myApplication: MyApplication? = null
        private var weatherDatabase: WeatherDatabase? = null
        fun getMyApp() = myApplication!!
        val retrofit = retrofitCreate()

        fun getWeatherDatabase(): WeatherDatabase {
            if (weatherDatabase == null) {
                weatherDatabase =
                    Room.databaseBuilder(getMyApp(), WeatherDatabase::class.java, ROOM_DATABASE)
                        .addMigrations(migration)
                        .build()
            }
            return weatherDatabase!!
        }

        private val migration = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE weather_entity_table ADD COLUMN icon  DEFAULT 0  NOT NULL ")
            }
        }

        private fun retrofitCreate(): WeatherApi {
            val retrofitImpl = Retrofit.Builder()
            retrofitImpl.baseUrl("https://api.weather.yandex.ru")
            retrofitImpl.addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            return retrofitImpl.build().create(WeatherApi::class.java)
        }
    }
}