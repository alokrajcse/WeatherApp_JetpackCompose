package com.sit.weatherapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val baseurl="https://api.weatherapi.com"

    fun getInstace(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    }

    val weatherApi: WeatherApi= getInstace().create(WeatherApi::class.java)

}