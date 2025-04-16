package com.example.cs388_group12_stockpath

import com.google.firebase.Timestamp
import java.util.UUID

data class Order(
    val uid: String = "Guest",
    val oid: String = newOID(),
    val sym: String = "test",
    val price: Double = 0.0,
    val qty: Double = 0.0,
    val type: String = "Buy",
    val timestamp: Timestamp = Timestamp.now()
) {
    //Firestore constructor noargs
    constructor() : this("", "", "", 0.0, 0.0, "", Timestamp.now())
    companion object {
        fun newOID(): String {
            return UUID.randomUUID().toString()
        }
    }
}