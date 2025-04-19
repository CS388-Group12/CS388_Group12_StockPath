package com.example.cs388_group12_stockpath
import com.google.firebase.Timestamp
data class Alert(
    val uid: String,
    val aid: String,
    val timestamp: Timestamp = Timestamp.now(),
    val sym: String,
    var watchPrice: Double,
    var currentPrice: Double,
    var watchType: String ? = "below" // can be above or below watchprice

) {
}
