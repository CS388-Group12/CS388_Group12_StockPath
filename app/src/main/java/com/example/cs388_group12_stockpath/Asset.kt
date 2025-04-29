package com.example.cs388_group12_stockpath
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
data class Asset(
    val sym: String,
    var totalQuantity: Double,
    var averagePrice: Double,
    var currentPrice: Double,
    var gainloss: Double,
    var orderCount: Int = 0
) {
     private val _currentPriceFlow = MutableStateFlow(currentPrice)
     val currentPriceFlow: StateFlow<Double> = _currentPriceFlow

     fun updatePrice(newPrice: Double) {
        if (newPrice != currentPrice) { // Only update if the price has changed
            currentPrice = newPrice
            _currentPriceFlow.value = newPrice
            Log.d("Asset", "Price updated for $sym: $newPrice")
        }
     }
    

    // fun startupdatingPrice(stockFuncs: StockFuncs) {
    //     CoroutineScope(Dispatchers.IO).launch {
    //         stockFuncs.get_current_price(sym, { price ->
    //             _currentPriceFlow.value = price
    //             Log.d("Asset", "Updated price for $sym: $price")

    //         }, { error ->
    //             Log.e("Asset", "Error updating price for $sym: $error")
    //         })


    //         while(true) {
    //             stockFuncs.get_current_price(sym, { price ->
    //                 _currentPriceFlow.value = price
    //                 Log.d("Asset", "Updated price for $sym: $price")

    //             }, { error ->
    //                 Log.e("Asset", "Error updating price for $sym: $error")
    //             })
    //             delay(60000) // Update every minute
    //         }
    //     }
    // }
}