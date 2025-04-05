package com.example.cs388_group12_stockpath.ui.alerts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlertsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is alerts Fragment"
    }
    val text: LiveData<String> = _text
}