package com.example.cs388_group12_stockpath

import android.util.Log

import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler

import okhttp3.Headers

class StockFuncs {
    private val API_KEY = BuildConfig.API_KEY

    //Symbol Search
    fun sym_search(sym: String, onSuccess: (List<String>) -> Unit, onError: (String) -> Unit) {
        val url = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=$sym&apikey=$API_KEY"

        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("StockFuncs", "Response: ${json.jsonObject}")

                try {
                    // Parse the JSON response using Gson
                    val results = json.jsonObject.getJSONArray("bestMatches")
                    val symbols = mutableListOf<String>()

                    for (i in 0 until results.length()) {
                        val match = results.getJSONObject(i)
                        val symbol = match.getString("1. symbol") // Adjust key based on API response
                        symbols.add(symbol)
                    }

                    onSuccess(symbols)
                } catch (e: Exception) {
                    Log.e("StockFuncs", "Error parsing response", e)
                    onError("Error parsing response")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                responseString: String?,
                throwable: Throwable?
            ) {
                Log.e("StockFuncs", "Request failed: $responseString", throwable)
                onError("Request failed: $responseString")
            }
        })
    }

    //GetCurrentPrice
    fun get_current_price(symbol: String, onSuccess: (Double) -> Unit, onError: (String) -> Unit) {
        val url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=$symbol&apikey=$API_KEY"

        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val globalQuote = json.jsonObject.getJSONObject("Global Quote")
                    val currentPrice = globalQuote.getString("05. price").toDouble()
                    onSuccess(currentPrice)
                } catch (e: Exception) {
                    Log.e("StockFuncs", "Error parsing current price", e)
                    onError("Error parsing current price")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                responseString: String?,
                throwable: Throwable?
            ) {
                Log.e("StockFuncs", "Request failed: $responseString", throwable)
                onError("Request failed: $responseString")
            }
        })
    }
}