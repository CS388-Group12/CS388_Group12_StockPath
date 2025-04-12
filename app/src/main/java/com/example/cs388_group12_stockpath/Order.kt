package com.example.cs388_group12_stockpath

data class Order(
    val sym: String,
    val price: Double,
    val qty: Double,
    val type: String,
    val timestamp: Long = System.currentTimeMillis()
)