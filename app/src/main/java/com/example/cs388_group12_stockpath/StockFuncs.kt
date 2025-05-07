package com.example.cs388_group12_stockpath

import android.util.Log
import android.icu.math.BigDecimal

import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler

import okhttp3.Headers
import org.json.JSONException

class StockFuncs {
    private val API_KEY = BuildConfig.API_KEY
    private val priceCache = mutableMapOf<String, Pair<Double, Long>>()
    private val updatingSymbols = mutableSetOf<String>()
    private var updateInterval: Long = 60000 

    //Symbol Search
    fun sym_search(sym: String, onSuccess: (List<String>) -> Unit, onError: (String) -> Unit) {
        val url = "https://query1.finance.yahoo.com/v1/finance/search?q=$sym&count=10&region=US&lang=en-US"
    
        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("StockFuncs", "Response: ${json.jsonObject}")
    
                try {
                    // Parse the JSON response
                    val results = json.jsonObject.optJSONArray("quotes")
                    if (results == null || results.length() == 0) {
                        onError("No matches found for the symbol: $sym")
                        return
                    }
    
                    val symbols = mutableListOf<String>()
                    for (i in 0 until results.length()) {
                        val match = results.getJSONObject(i)
                        val symbol = match.optString("symbol", null)
                        if (symbol != null) {
                            symbols.add(symbol)
                        }
                    }
    
                    if (symbols.isEmpty()) {
                        onError("No valid symbols found for the search query: $sym")
                    } else {
                        onSuccess(symbols)
                    }
                } catch (e: Exception) {
                    Log.e("StockFuncs", "Error parsing response", e)
                    onError("Error parsing response: ${e.message}")
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

    fun setUpdateFrequency(intervalMillis: Long) {
        updateInterval = intervalMillis
        Log.d("StockFuncs", "Update freq set to $updateInterval ms")
    }

    //GetCurrentPrice
    fun get_current_price(symbol: String, onSuccess: (Double) -> Unit, onError: (String) -> Unit) {
        val url = "https://query1.finance.yahoo.com/v8/finance/chart/$symbol?interval=1d&range=1m"
        val currentTime = System.currentTimeMillis()
        val cachedPrice = priceCache[symbol]
    
        //check priced already cached and valid
        if (cachedPrice != null && currentTime - cachedPrice.second < updateInterval) {
            Log.d("StockFuncs", "Skipping API call. Using cached price for $symbol: ${cachedPrice.first}")
            onSuccess(cachedPrice.first)
            return
        }
    
        //prevent redundant fetches for the same symbol
        synchronized(updatingSymbols) {
            if (updatingSymbols.contains(symbol)) {
                Log.d("StockFuncs", "Skipping redundant fetch. $symbol is already in progress. ")
                return
            }
            updatingSymbols.add(symbol)
        }
        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    Log.d("StockFuncs", "API Response for $symbol: ${json.jsonObject}")
                    val result = json.jsonObject.getJSONObject("chart").getJSONArray("result").getJSONObject(0)
                    val closingPrices = result.getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("close")
                    val currentPrice = BigDecimal(closingPrices.optDouble(closingPrices.length() - 1, -1.0))
                        .toDouble().toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
                    if (currentPrice == -1.0) {
                        throw JSONException("Invalid closing price")
                    }
    
                    //update cache
                    priceCache[symbol] = Pair(currentPrice, currentTime)
                    Log.d("StockFuncs", "Fetched and cached current price for $symbol: $currentPrice")
                    onSuccess(currentPrice)
                } catch (e: Exception) {
                    Log.e("StockFuncs", "Error parsing current price for $symbol", e)
                    onError("Error parsing current price for $symbol")
                } finally {
                    synchronized(updatingSymbols) {
                        updatingSymbols.remove(symbol)
                    }
                }
            }
    
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                responseString: String?,
                throwable: Throwable?
            ) {
                Log.e("StockFuncs", "Request failed for $symbol: $responseString", throwable)
                onError("Request failed: $responseString")
                synchronized(updatingSymbols) {
                    updatingSymbols.remove(symbol)
                }
            }
        })
    }

    //GetHistoricalData
    fun get_historical_data(
        symbol: String,
        interval: String,
        range: String,
        onSuccess: (List<Candlestick>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "https://query1.finance.yahoo.com/v8/finance/chart/$symbol?interval=$interval&range=$range"
        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val result = json.jsonObject
                        .getJSONObject("chart")
                        .getJSONArray("result")
                        .getJSONObject(0)
                    val indicators = result.getJSONObject("indicators").getJSONArray("quote").getJSONObject(0)
    
                    val opens = indicators.getJSONArray("open")
                    val highs = indicators.getJSONArray("high")
                    val lows = indicators.getJSONArray("low")
                    val closes = indicators.getJSONArray("close")
    
                    val candlesticks = mutableListOf<Candlestick>()

                    val timestamps = result.getJSONArray("timestamp")
                    val volumes = indicators.getJSONArray("volume")

                    for (i in 0 until opens.length()) {
                        val open = opens.optDouble(i, Double.NaN)
                        val high = highs.optDouble(i, Double.NaN)
                        val low = lows.optDouble(i, Double.NaN)
                        val close = closes.optDouble(i, Double.NaN)
                        val timestamp = timestamps.optLong(i, 0)
                        val volume = volumes.optDouble(i, Double.NaN)

                        if (!open.isNaN() && !high.isNaN() && !low.isNaN() && !close.isNaN() && timestamp != 0L && !volume.isNaN()) {
                            candlesticks.add(Candlestick(timestamp, open, high, low, close, volume))
                        }
                    }
    
                    onSuccess(candlesticks)
                } catch (e: JSONException) {
                    onError("Error parsing historical data: ${e.message}")
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

    fun fetchNews(
    sym: String,
    onSuccess: (List<NewsItem>) -> Unit,
    onError: (String) -> Unit
    ) {
        val url = "https://query1.finance.yahoo.com/v1/finance/search?q=$sym&count=10&region=US&lang=en-US"

        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.d("StockFuncs", "Response: ${json.jsonObject}")

                try {
                    // Parse the JSON response for news
                    val newsArray = json.jsonObject.optJSONArray("news")
                    val newsItems = mutableListOf<NewsItem>()
                    if (newsArray != null) {
                        for (i in 0 until newsArray.length()) {
                            val newsObject = newsArray.getJSONObject(i)
                            val title = newsObject.optString("title", "No Title")
                            val link = newsObject.optString("link", "")
                            val publisher = newsObject.optString("publisher", "Unknown Publisher")
                            newsItems.add(NewsItem(title, link, publisher))
                        }
                    }

                    if (newsItems.isEmpty()) {
                        onError("No news found for the symbol: $sym")
                    } else {
                        onSuccess(newsItems)
                    }
                } catch (e: Exception) {
                    Log.e("StockFuncs", "Error parsing response", e)
                    onError("Error parsing response: ${e.message}")
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

    //GetNews
    // fun get_news(
    //     symbol: String,
    //     onSuccess: (List<String>) -> Unit,
    //     onError: (String) -> Unit
    // ) {
    //     val url = "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=$symbol&apikey=$API_KEY"
    //     val client = AsyncHttpClient()
    //     client.get(url, object : JsonHttpResponseHandler() {
    //         override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
    //             try {
    //                 val news = json.jsonObject.getJSONArray("feed")
    //                 val articles = mutableListOf<String>()

    //                 for (i in 0 until news.length()) {
    //                     val article = news.getJSONObject(i)
    //                     val title = article.getString("title")
    //                     articles.add(title)
    //                 }

    //                 onSuccess(articles)
    //             } catch (e: Exception) {
    //                 Log.e("StockFuncs", "Error parsing news", e)
    //                 onError("Error parsing news")
    //             }
    //         }

    //         override fun onFailure(
    //             statusCode: Int,
    //             headers: Headers?,
    //             responseString: String?,
    //             throwable: Throwable?
    //         ) {
    //             Log.e("StockFuncs", "Request failed: $responseString", throwable)
    //             onError("Request failed: $responseString")
    //         }
    //     })
    // }
    // //GetCompanyOverview
    // fun get_company_overview(
    //     symbol: String,
    //     onSuccess: (String) -> Unit,
    //     onError: (String) -> Unit
    // ) {
    //     val url = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=$symbol&apikey=$API_KEY"
    //     val client = AsyncHttpClient()
    //     client.get(url, object : JsonHttpResponseHandler() {
    //         override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
    //             try {
    //                 val overview = json.jsonObject
    //                 val description = overview.getString("Description")
    //                 onSuccess(description)
    //             } catch (e: Exception) {
    //                 Log.e("StockFuncs", "Error parsing company overview", e)
    //                 onError("Error parsing company overview")
    //             }
    //         }

    //         override fun onFailure(
    //             statusCode: Int,
    //             headers: Headers?,
    //             responseString: String?,
    //             throwable: Throwable?
    //         ) {
    //             Log.e("StockFuncs", "Request failed: $responseString", throwable)
    //             onError("Request failed: $responseString")
    //         }
    //     })
    // }
}