package com.kartheek.weatherapp.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kartheek.weatherapp.R
import com.kartheek.weatherapp.data.NetworkResult
import kotlinx.coroutines.launch

class WeatherActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is NetworkResult.Success -> {

                        }
                        is NetworkResult.Loading -> {

                        }
                        is NetworkResult.Error -> {
                            //Handle Error
                        }
                    }
                }
            }
        }
    }
}