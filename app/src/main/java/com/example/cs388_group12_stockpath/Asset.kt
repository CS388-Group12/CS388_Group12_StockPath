package com.example.cs388_group12_stockpath

data class Asset(
    val sym: String,
    var totalQuantity: Double,
    var averagePrice: Double,
    var currentPrice: Double,
    var gainloss: Double,
    var orderCount: Int = 0
) {
}