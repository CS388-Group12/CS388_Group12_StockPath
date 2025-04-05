package com.example.cs388_group12_stockpath.ui.charts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChartsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is charts Fragment"
    }
    val text: LiveData<String> = _text
}