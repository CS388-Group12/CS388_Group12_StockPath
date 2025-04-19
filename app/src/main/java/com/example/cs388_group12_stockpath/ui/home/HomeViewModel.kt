package com.example.cs388_group12_stockpath.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cs388_group12_stockpath.GlobalUserView
import kotlin.properties.ReadOnlyProperty

class HomeViewModel : ViewModel() {
    val email = GlobalUserView().getUserEmail()


    private val _text = MutableLiveData<String>().apply {
        value = "${email}'s Portfolio"
    }
    val text: LiveData<String> = _text
}