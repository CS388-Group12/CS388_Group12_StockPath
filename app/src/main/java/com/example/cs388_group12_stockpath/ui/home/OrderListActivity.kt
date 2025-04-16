package com.example.cs388_group12_stockpath.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs388_group12_stockpath.GlobalUserView
import com.example.cs388_group12_stockpath.Order
import com.example.cs388_group12_stockpath.R
import com.example.cs388_group12_stockpath.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class OrderListActivity : AppCompatActivity() {

    private val globalUserView: GlobalUserView by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        val user_email_text: TextView = findViewById(R.id.user_email_text)
        val auth_button: Button = findViewById(R.id.auth_button)

        val assetSym = intent.getStringExtra("asset_sym") ?: "test"
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

//        supportActionBar?.apply {
//            title = "Orders for $assetSym"
//            setDisplayHomeAsUpEnabled(true)
//        }
        
        globalUserView.user.observe(this) { user ->
        if (user != null) {
            user_email_text.text = "Welcome: ${user.email}"
            auth_button.text = "Sign Out"
            auth_button.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                globalUserView.refreshUser()
            }
        } else {
            user_email_text.text = "Welcome: Guest"
            auth_button.text = "Sign In"
            auth_button.setOnClickListener {
                // Navigate to sign-in activity
                startActivity(Intent(this, RegisterActivity::class.java))
                }
            }
        }

        globalUserView.orders.observe(this) { orders ->
            val filteredOrders = orders.filter { it.sym == assetSym }
            Log.d("OrderListActivity", "Filtered orders: $filteredOrders")
            Log.d("OrderListActivity", "UnfilteredOrders: $orders")
            val adapter = OrderAdapter(filteredOrders)
            recyclerView.adapter = adapter
        }
//        globalUserView.uid.observe(this) { guid ->
//        if (guid != null) {
//            val sampleOrders = listOf(
//                Order(uid = guid, oid = "1", sym = "AAPL", price = 150.0, qty = 10.0, type = "Buy"),
//                Order(uid = guid, oid = "2", sym = "GOOGL", price = 2800.0, qty = 5.0, type = "Sell"),
//                Order(uid = guid, oid = "3", sym = "MSFT", price = 160.0, qty = 8.0, type = "Buy"),
//                Order(uid = guid, oid = "4", sym = "GOOGL", price = 2900.0, qty = 3.0, type = "Sell"),
//                Order(uid = guid, oid = "5", sym = "MSFT", price = 130.0, qty = 9.5, type = "Buy")
//            )
//            val adapter = OrderAdapter(sampleOrders)
//            recyclerView.adapter = adapter
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}