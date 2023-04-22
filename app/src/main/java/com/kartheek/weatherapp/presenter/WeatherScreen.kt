package com.kartheek.weatherapp.presenter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kartheek.weatherapp.common.Constants
import com.kartheek.weatherapp.R
import com.kartheek.weatherapp.common.showToast
import com.kartheek.weatherapp.data.NetworkResult
import com.kartheek.weatherapp.data.model.WeatherRes
import androidx.compose.foundation.Image
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter


@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val searchCityState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(30.dp))
        SearchField(viewModel)
        Spacer(modifier = Modifier.height(20.dp))
        SearchCityScreenContent(
            viewModel = viewModel, searchCityState = searchCityState
        )
    }

}

@Composable
private fun SearchField(viewModel: WeatherViewModel) {
    OutlinedTextField(
        value = viewModel.searchFieldValue,
        onValueChange = { viewModel.updateSearchField(it) },
        label = {
            Text(text = Constants.searchCityPlaceHolder)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        maxLines = 1,
        trailingIcon =  {
            IconButton(onClick = { viewModel.searchCityClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_24),
                    contentDescription = "Visibility Icon"
                )
            }
        }
    )
}


@Composable
private fun SearchCityScreenContent(
    viewModel: WeatherViewModel,
    searchCityState: NetworkResult<WeatherRes>
) {
    if (viewModel.isCitySearched) {
        when (searchCityState) {
            is NetworkResult.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressBar(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }
            }
            is NetworkResult.Success -> {
                if (searchCityState.data != null) {
                    SearchCityContentUI(searchCityState.data, viewModel)
                }
            }
            is NetworkResult.Error -> {
                val context = LocalContext.current
                context.showToast("Unable to fetch weather from your location, please try again")
            }
        }
    }

}

@Composable
private fun  SearchCityContentUI(
data:WeatherRes, viewModel: WeatherViewModel,
){
    val imageUrl = "https://openweathermap.org/img/wn/${data.weather[0].icon}.png"
    Column() {
        Card(
            elevation = 4.dp,
            backgroundColor = colorResource(id = R.color.purple_200)
        ){
            Row() {
                Column(Modifier.padding(10.dp)) {
                    Text(modifier = Modifier.padding(start = 16.dp) ,text = data.name, fontSize = 16.sp, color = colorResource(id = R.color.white))
                    Text(modifier = Modifier.padding(start = 16.dp) ,text = "${data.main.temp.toInt()}${Constants.degree}", fontSize = 76.sp, color = colorResource(id = R.color.white))
                }
                WeatherStateImage(imageUrl)
            }

        }
    }
}

@Composable
fun WeatherStateImage(
    imagerUrl: String
) {
    Image(
        painter = rememberAsyncImagePainter(imagerUrl),
        contentDescription = "Image",
        modifier = Modifier.size(80.dp)
    )
}