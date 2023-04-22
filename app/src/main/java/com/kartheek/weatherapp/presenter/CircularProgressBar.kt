package com.kartheek.weatherapp.presenter

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.kartheek.weatherapp.R

@Composable
fun CircularProgressBar(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = colorResource(id = R.color.purple_200),
        strokeWidth = 5.dp
    )
}