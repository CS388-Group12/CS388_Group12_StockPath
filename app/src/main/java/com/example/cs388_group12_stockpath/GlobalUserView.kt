package com.example.cs388_group12_stockpath

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class GlobalUserView : ViewModel() { //global user data model to be persistant and asynchrounous accross app
    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> = _user
    private val _uid = MutableLiveData<String?>() //?: "Guest" as MutableLiveData<String?>
    val uid: LiveData<String?> = _uid //?: "Guest" as LiveData<String?>
    private val _email = MutableLiveData<String?>() //?: "Guest@StockPath" as MutableLiveData<String?>
    val email: LiveData<String?> = _email //?: "Guest@StockPath" as LiveData<String?>
    init {
        getUser()
    }

    private fun getUser() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            _user.value = auth.currentUser
        }
        Log.d("GlobalUserView", "Current user: ${user.value}")
        updateUserdata()
    }
    private fun updateUserdata(){
        _uid.value = user.value?.uid ?: "Guest"
        _email.value = user.value?.email ?: "Guest@StockPath"
        Log.d("GlobalUserView", "Updated user data: uid: ${uid.value}, email: ${email.value}")
    }

    fun refreshUser() {
        getUser()
        Log.d("GlobalUserView", "Refreshed user data: uid: ${uid.value}, email: ${email.value}")
    }

}