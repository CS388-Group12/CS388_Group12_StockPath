package com.example.cs388_group12_stockpath

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class GlobalUserView : ViewModel() { //global user data model to be persistant and asynchrounous accross app
    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> = _user
    private val _uid = MutableLiveData<String?>() //?: "Guest" as MutableLiveData<String?>
    val uid: LiveData<String?> = _uid //?: "Guest" as LiveData<String?>
    private val _email = MutableLiveData<String?>() //?: "Guest@StockPath" as MutableLiveData<String?>

    val email: LiveData<String?> = _email //?: "Guest@StockPath" as LiveData<String?>



    //Home Fragment Portfolio
    private val stockFuncs = StockFuncs()
    private var _orders = MutableLiveData<MutableList<Order>>(mutableListOf())
    var orders: LiveData<MutableList<Order>> = _orders
    private var _assets = MutableLiveData<MutableList<Asset>>(mutableListOf())
    var assets: LiveData<MutableList<Asset>> = _assets

    init {
        getUser()
        getOrders()
        _orders.observeForever { orders ->
        calculateAssetsFromOrders()
        }
    }

    private fun getUser() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            _user.value = auth.currentUser
        }
        Log.d("GlobalUserView", "Current user: ${user.value}")
        updateUserdata()
    }

    fun getUserEmail(): String {
        return email.value ?: "Guest@StockPath"
    }

    private fun updateUserdata(){
        _uid.value = user.value?.uid ?: "Guest"
        _email.value = user.value?.email ?: "Guest@StockPath"
        Log.d("GlobalUserView", "Updated user data: uid: ${uid.value}, email: ${email.value}")
    }

    fun refreshUser() {
        getUser()
        getOrders()
        _orders.observeForever { orders ->
        calculateAssetsFromOrders()
        }
        Log.d("GlobalUserView", "Refreshed user data: uid: ${uid.value}, email: ${email.value}")
    }

    //Orders
    fun getOrders() {
    val uid = _uid.value ?: return
    val db = FirebaseFirestore.getInstance()
    val ordersRef = db.collection("Users").document(uid).collection("Orders")

//    ordersRef.get()
//        .addOnSuccessListener { documents ->
//            val ordersList = mutableListOf<Order>()
//            for (document in documents) {
//                val order = document.toObject(Order::class.java)
//                ordersList.add(order)
//            }
//            _orders.value = ordersList
//            Log.d("GlobalUserView", "Fetched orders: $ordersList")
//        }
//        .addOnFailureListener { exception ->
//            Log.e("GlobalUserView", "Error fetching orders", exception)
//        }
    ordersRef.addSnapshotListener { snapshots, e ->
        if (e != null) {
            Log.e("GlobalUserView", "Error listening for orders", e)
            return@addSnapshotListener
        }

        val ordersList = mutableListOf<Order>()
        for (document in snapshots!!) {
            val order = document.toObject(Order::class.java)
            ordersList.add(order)
        }
        _orders.value = ordersList
        Log.d("GlobalUserView", "Real-time fetched orders: $ordersList")
    }
}

fun putOrder(order: Order) {
    val uid = _uid.value ?: return
    val db = FirebaseFirestore.getInstance()
    val userRef = db.collection("Users").document(uid)
    val ordersRef = userRef.collection("Orders")

    //if uid document exists
    userRef.get()
        .addOnSuccessListener { document ->
            if (!document.exists()) {
                //create uid document if doesnt exist
                userRef.set(mapOf("initialized" to true))
                    .addOnSuccessListener {
                        Log.d("GlobalUserView", "User document created for uid: $uid")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("GlobalUserView", "Error creating user document", exception)
                    }
            }

            //update add to orders collection
            ordersRef.document(order.oid).set(order)
                .addOnSuccessListener {
                    Log.d("GlobalUserView", "Order successfully added: $order")
                }
                .addOnFailureListener { exception ->
                    Log.e("GlobalUserView", "Error adding order", exception)
                }
        }
        .addOnFailureListener { exception ->
            Log.e("GlobalUserView", "Error checking user document", exception)
        }
    }

    //Assets derrived from orders
    fun calculateAssetsFromOrders() {
        val ordersList = _orders.value ?: return
        val assetsMap = mutableMapOf<String, Asset>()

        for (order in ordersList) {
            val sym = order.sym
            val asset = assetsMap[sym] ?: Asset(sym, 0.0, 0.0, 0.0, 0.0)

            //update total quantity and average price
            if (order.type == "Buy") {
                val totalCost = asset.totalQuantity * asset.averagePrice + order.qty * order.price
                asset.totalQuantity += order.qty
                asset.averagePrice = if (asset.totalQuantity > 0) totalCost / asset.totalQuantity else 0.0
            } else if (order.type == "Sell") {
                asset.totalQuantity -= order.qty
                if (asset.totalQuantity < 0) asset.totalQuantity = 0.0 //positive quantities
            }

            asset.orderCount += 1

            //updated asset back to the map
            assetsMap[sym] = asset
        }

        //update asset list
        _assets.value = assetsMap.values.toMutableList()
    }




}
    