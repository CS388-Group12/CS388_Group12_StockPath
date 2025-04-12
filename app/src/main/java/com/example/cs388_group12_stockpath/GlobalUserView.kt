package com.example.cs388_group12_stockpath
import com.example.cs388_group12_stockpath.Order
import com.example.cs388_group12_stockpath.Asset
import com.example.cs388_group12_stockpath.StockFuncs
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

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

    //Home Fragment Portfolio
    private val stockFuncs = StockFuncs()

    private val _orders = MutableLiveData<MutableList<Order>>(mutableListOf())
    val orders: LiveData<MutableList<Order>> = _orders

    private val _assets = MutableLiveData<MutableList<Asset>>(mutableListOf())
    val assets: LiveData<MutableList<Asset>> = _assets

    fun addOrder(order: Order) {
        // Add the order to the orders list
        _orders.value?.add(order)
        _orders.postValue(_orders.value) // Notify observers

        // Update the assets list
        val existingAsset = _assets.value?.find { it.sym == order.sym }
        if (existingAsset != null) {
            // Update the existing asset
            val totalQuantity = existingAsset.totalQuantity + order.qty
            val totalValue =
                (existingAsset.averagePrice * existingAsset.totalQuantity) + (order.price * order.qty)
            existingAsset.totalQuantity = totalQuantity
            existingAsset.averagePrice = totalValue / totalQuantity
        } else {
            // Add a new asset
            val newAsset = Asset(
                sym = order.sym,
                totalQuantity = order.qty,
                averagePrice = order.price,
                currentPrice =0.0,
                gainloss = 0.0
            )
            _assets.value?.add(newAsset)
        }
        //getcurrentprice and calculate gain/loss
        _assets.value?.forEach { asset ->
            stockFuncs.get_current_price(asset.sym, { currentPrice: Double ->
                asset.currentPrice = currentPrice
                asset.gainloss = (currentPrice - asset.averagePrice) * asset.totalQuantity
                _assets.postValue(_assets.value) // Notify observers
            }, { error ->
                Log.e("GlobalUserView", "Error fetching current price for ${asset.sym}: $error")
            })
        }

        _assets.postValue(_assets.value) // Notify observers

        // Store the order in Firebase
        val uid = _uid.value
        if (uid != null) {
            val database = FirebaseDatabase.getInstance().getReference("users/$uid/portfolio")
            database.push().setValue(order)
                .addOnSuccessListener {
                    Log.d("GlobalUserView", "Order successfully added to Firebase")
                }
                .addOnFailureListener { e ->
                    Log.e("GlobalUserView", "Failed to add order to Firebase", e)
                }
        }
    }


    fun refreshUser() {
        getUser()
        Log.d("GlobalUserView", "Refreshed user data: uid: ${uid.value}, email: ${email.value}")
    }

}