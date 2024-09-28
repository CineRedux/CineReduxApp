package com.example.cineredux_v2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    val cachedMovies = MutableLiveData<List<Movie>>()
}