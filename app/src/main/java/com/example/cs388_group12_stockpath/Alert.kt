package com.example.cs388_group12_stockpath
import com.google.firebase.Timestamp
import java.util.UUID

data class Alert(
    val uid: String = "Guest",
    val aid: String = newAID(),
    val timestamp: Timestamp,
    val sym: String = "test",
    var watchPrice: Double = 0.0,
    var currentPrice: Double,
    var watchType: String ? = "below" //watchprice can be above or below

) {
    //Firestore constructor noargs
    constructor() : this("", "", Timestamp.now(), "", 0.0, 0.0, "")
    companion object {
        fun newAID(): String {
            return UUID.randomUUID().toString()
        }
    }
}