package com.sit.weatherapp

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sit.weatherapp.api.Constant
import com.sit.weatherapp.api.NetworkResponse
import com.sit.weatherapp.api.RetrofitInstance
import com.sit.weatherapp.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {


    val weatherApi=RetrofitInstance.weatherApi
    val _weatherResult=MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city: String){
        
        
        Log.i("Cityeeeee: ",city)
        _weatherResult.value=NetworkResponse.Loading

        viewModelScope.launch {

          try {
              val response=weatherApi.getWeather(Constant.apikey,city)
              if (response.isSuccessful)
              {

                  response.body()?.let {
                      _weatherResult.value=NetworkResponse.Success(it)
                  }
                  //  Toast.makeText(LocalContext, "Sucess", Toast.LENGTH_SHORT).show()

                  Log.i("Responseee: ",response.body().toString())
              }
              else
              {
                  Log.i("Erroreee: ",response.errorBody().toString())

                  _weatherResult.value=NetworkResponse.Error("Failed to load data")
              }
          }
          catch (e: Exception){

              _weatherResult.value=NetworkResponse.Error("Failed to load data")
          }

        }




    }
}

@Preview
@Composable
private fun check() {

    Text(text = "Hello")

}