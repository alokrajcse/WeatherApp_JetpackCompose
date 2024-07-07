package com.sit.weatherapp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.sit.weatherapp.api.NetworkResponse
import com.sit.weatherapp.api.WeatherModel


@Preview
@Composable
fun WeatherPage(viewModel: WeatherViewModel,modifier: Modifier = Modifier) {

    var city by remember {
        mutableStateOf("")
    }

    val weatherResult=viewModel.weatherResult.observeAsState()

    val keyboardController=LocalSoftwareKeyboardController.current

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row (verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)){
            OutlinedTextField(
                value = city,
                onValueChange = {
                city=it
            }, label = {
                Text("Enter City")
                },
                modifier = Modifier.weight(1f))

            IconButton(onClick = { viewModel.getData(city)

                city=""
                keyboardController?.hide()


            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
                
            }
        }


        when(val result=weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                //Text(text = result.data.toString())
                WeatherDetails(data = result.data)
            }
            null -> {}
        }


    }
    
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherDetails(data:WeatherModel, modifier: Modifier = Modifier) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "", modifier = Modifier.size(32.dp))
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 22.sp, color = Color.Gray)

        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = data.current.temp_c.toString(), fontSize = 40.sp)

//        AsyncImage(
//            model="https:${data.current.condition.icon}",
//            contentDescription = "",
//            modifier = Modifier.size(128.dp)
//        )

        GlideImage(model = "https:${data.current.condition.icon}", loading = placeholder(ColorPainter(Color.Red)), contentDescription = "", modifier = Modifier.size(64.dp))

        Text(text = data.current.condition.text, fontSize = 15.sp, textAlign = TextAlign.Center, color = Color.Gray)

        Spacer(modifier = Modifier.height(18.dp))
        Card {
            Column (modifier = Modifier.fillMaxWidth()){

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.SpaceAround) {

                    

                    WeatherKeyVal(key = "Humidity", value = data.current.humidity )
                    WeatherKeyVal(key = "Wind", value = data.current.wind_kph.toString()+" km/h")

//                    Column {
//
//                        Text(text = data.current.humidity)
//                        Text(text = "Humidity")
//
//                    }

                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.SpaceAround) {



                    WeatherKeyVal(key = "UV", value = data.current.uv )
                    WeatherKeyVal(key = "Precipitation", value = data.current.precip_mm.toString()+" mm")



                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.SpaceAround) {



                    WeatherKeyVal(key = "Local Time", value = data.location.localtime.split("")[1] )
                    WeatherKeyVal(key = "Local Date", value = data.location.localtime.split("")[0])



                }

            }
        }


    }
    
}

@Composable
fun WeatherKeyVal(key: String, value: String, modifier: Modifier = Modifier) {


    Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text =key,fontSize = 15.sp, color = Color.Gray)

    }
    
}