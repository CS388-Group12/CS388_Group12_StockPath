package com.example.cs388_group12_stockpath

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GlobalUserView : ViewModel() { 
    // StockFuncs Section
    private val stockFuncs = StockFuncs()

    // User Login Section
    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> = _user
    private val _uid = MutableLiveData<String?>()
    val uid: LiveData<String?> = _uid
    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> = _email

    // Home Fragment Portfolio
    private val _orders = MutableLiveData<MutableList<Order>>(mutableListOf())
    val orders: LiveData<MutableList<Order>> = _orders
    private val _assets = MutableLiveData<MutableList<Asset>?>(mutableListOf())
    val assets: LiveData<MutableList<Asset>?> = _assets
    private val _alerts = MutableLiveData<MutableList<Alert>>(mutableListOf())
    val alerts: LiveData<MutableList<Alert>> = _alerts

    // Current Prices
    // private val _currentPriceSet = MutableLiveData<Map<String, Double>>(emptyMap())
    // val currentPriceSet: LiveData<Map<String, Double>> = _currentPriceSet

    // private val _priceCache = mutableMapOf<String, Double>()
    // val priceCache: Map<String, Double> get() = _priceCache
    private val _priceCache = MutableLiveData<MutableMap<String, Double>>(mutableMapOf())
    val priceCache: LiveData<MutableMap<String, Double>> = _priceCache


    init {
        viewModelScope.launch {
            initializeUser()
            fetchOrdersAsync().await()

            startUpdatingPricesAsync().await()
        }

    }

    // Refresh User Data
    fun refreshUser(){
        viewModelScope.launch {
        
            val auth = FirebaseAuth.getInstance()
            _user.value = auth.currentUser
            updateUserdata()
            Log.d("GlobalUserView", "Refreshed user: ${user.value}")
            fetchOrdersAsync().await()
            startUpdatingPricesAsync().await()
        }
    }


    // Initialize User Data
    private fun initializeUser() {
        val auth = FirebaseAuth.getInstance()
        _user.value = auth.currentUser
        updateUserdata()
        Log.d("GlobalUserView", "Initialized user: ${user.value}")
    }

    private fun updateUserdata() {
        _uid.value = user.value?.uid ?: "Guest"
        _email.value = user.value?.email ?: "Guest@StockPath"
        Log.d("GlobalUserView", "Updated user data: uid: ${uid.value}, email: ${email.value}")
    }

    fun getUserEmail(): String {
        return _email.value ?: "Guest@StockPath"
    }

    

    // Get Price from Cache
    fun getPrice(symbol: String): Double {
        return priceCache.value?.get(symbol) ?: 0.0
    }

    // Add or Update Orders in Firestore
    fun putOrder(order: Order) {
        val uid = _uid.value ?: return
        val db = FirebaseFirestore.getInstance()
        val ordersRef = db.collection("Users").document(uid).collection("Orders")

        ordersRef.document(order.oid).set(order)
            .addOnSuccessListener {
                Log.d("GlobalUserView", "Order added/updated: $order")
                calculateAssetsFromOrders() // Recalculate assets after adding/updating an order
            }
            .addOnFailureListener { exception ->
                Log.e("GlobalUserView", "Error adding/updating order", exception)
            }
    }

    // Fetch Orders and Listen for Updates
    fun fetchOrdersAsync() = viewModelScope.async {
        val uid = _uid.value ?: return@async
        val db = FirebaseFirestore.getInstance()
        val ordersRef = db.collection("Users").document(uid).collection("Orders")

        ordersRef.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.e("GlobalUserView", "Error listening for orders", e)
                return@addSnapshotListener
            }

            val ordersList = snapshots?.mapNotNull { it.toObject(Order::class.java) }?.toMutableList() ?: mutableListOf()
            _orders.value = ordersList
            Log.d("GlobalUserView", "Fetched orders: $ordersList")
            startUpdatingPricesAsync()
        }
    }

    // Lock object for synchronizing access to priceCache
    private val priceCacheLock = Any()

    // Start Updating Prices Periodically
    fun startUpdatingPricesAsync() = viewModelScope.async {
            var isInit = true //flag
            while (true) {
                val ordersList = _orders.value ?: emptyList()
                val symbols = ordersList.map { it.sym }.distinct()

                if (symbols.isNotEmpty()) {
                    coroutineScope {
                        symbols.map { symbol ->
                            async {
                                stockFuncs.get_current_price(symbol, { price ->
                                    if (price > 0) {
                                        synchronized(priceCacheLock) { 
                                            val updatedPrices = priceCache.value ?: mutableMapOf()
                                            updatedPrices[symbol] = price
                                            _priceCache.postValue(updatedPrices)
                                            //_priceCache[symbol] = price
                                        }
                                        Log.d("GlobalUserView", "Fetched updated price for $symbol: $price")
                                    }
                                }, { error ->
                                    Log.e("GlobalUserView", "Error fetching price for $symbol: $error")
                                })
                            }
                        }.forEach { it.await() }
                    }

                    calculateAssetsFromOrders() //
                }

                if (isInit) {
                    isInit = false //completed fistrun
                    Log.d("GlobalUserView", "First run completed no delay")
                } else if (symbols.isNotEmpty()) {
                    Log.d("GlobalUserView", "Delay 60 seconds.")
                    delay(60000) // Wait for 60 seconds before the next update
                } else {
                    Log.d("GlobalUserView", "Delay 10s skipping update: No symbols to update Not init.")
                    delay(1000) // Wait for 10 seconds if no symbols are found
                }

            }
        
    }

    // Calculate Assets from Orders
    fun calculateAssetsFromOrders() {
        val ordersList = _orders.value ?: return
        val assetsMap = mutableMapOf<String, Asset>()
        for (order in ordersList) {
            val sym = order.sym
            if (sym.isBlank()) {
                Log.e("GlobalUserView", "Skipping invalid order with blank symbol: $order")
                continue
            }

            val asset = assetsMap[sym] ?: Asset(
                sym = sym,
                totalQuantity = 0.0,
                averagePrice = 0.0,
                currentPrice = priceCache.value?.get(sym) ?: 0.0,
                gainloss = 0.0
            )

            if (order.type == "Buy") {
                val totalCost = asset.totalQuantity * asset.averagePrice + order.qty * order.price
                asset.totalQuantity += order.qty
                asset.averagePrice = if (asset.totalQuantity > 0) totalCost / asset.totalQuantity else 0.0
            } else if (order.type == "Sell") {
                asset.totalQuantity -= order.qty
                if (asset.totalQuantity < 0) asset.totalQuantity = 0.0
            }

            asset.orderCount += 1

            // Update currentPrice only if a valid price exists in priceCache
            val newPrice = priceCache.value?.get(sym)?: 0.0
            if (newPrice > 0.0) {
                asset.currentPrice = newPrice
            }

            assetsMap[sym] = asset
        }

        _assets.postValue(assetsMap.values.toMutableList())
        Log.d("GlobalUserView", "Calculated assets: ${assetsMap.values}")
    }

    // Fetch Alerts and Listen for Updates
    fun getAlerts() {
        val uid = _uid.value ?: return
        val db = FirebaseFirestore.getInstance()
        val alertsRef = db.collection("Users").document(uid).collection("Alerts")

        alertsRef.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.e("GlobalUserView", "Error listening for alerts", e)
                return@addSnapshotListener
            }

            val alertsList = snapshots?.mapNotNull { it.toObject(Alert::class.java) }?.toMutableList() ?: mutableListOf()
            _alerts.value = alertsList
            Log.d("GlobalUserView", "Fetched alerts: $alertsList")
        }
    }

    // Add Alert
    fun addAlert(alert: Alert) {
        val uid = _uid.value ?: return
        val db = FirebaseFirestore.getInstance()
        val alertsRef = db.collection("Users").document(uid).collection("Alerts")

        alertsRef.document(alert.aid).set(alert)
            .addOnSuccessListener {
                Log.d("GlobalUserView", "Alert added: $alert")
            }
            .addOnFailureListener { exception ->
                Log.e("GlobalUserView", "Error adding alert", exception)
            }
    }
}