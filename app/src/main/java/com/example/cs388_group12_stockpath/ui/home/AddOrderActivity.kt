package com.example.cs388_group12_stockpath

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cs388_group12_stockpath.Order

class AddOrderActivity : AppCompatActivity() {

    private val stockFuncs = StockFuncs()
    private val globalUserView: GlobalUserView by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_order)

        val searchEditText: EditText = findViewById(R.id.editTextSearchAsset)
        val searchButton: Button = findViewById(R.id.buttonSearch)
        val resultsListView: ListView = findViewById(R.id.listViewResults)
        val selectedAssetTextView: TextView = findViewById(R.id.textViewSelectedAsset)
        val priceEditText: EditText = findViewById(R.id.editTextPrice)
        val qtyEditText: EditText = findViewById(R.id.editTextQty)
        val buyRadioButton: RadioButton = findViewById(R.id.radioBuy)
        val submitButton: Button = findViewById(R.id.buttonSubmitOrder)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                stockFuncs.sym_search(query, { symbols ->
                    runOnUiThread {
                        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, symbols)
                        resultsListView.adapter = adapter
                    }
                }, { error ->
                    runOnUiThread {
                        Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter an asset name to search", Toast.LENGTH_SHORT).show()
            }
        }

        resultsListView.setOnItemClickListener { _, _, position, _ ->
            val selectedAsset = resultsListView.getItemAtPosition(position).toString()
            selectedAssetTextView.text = selectedAsset
        }

        submitButton.setOnClickListener {
            val selectedAsset = selectedAssetTextView.text.toString()
            val price = priceEditText.text.toString().toDoubleOrNull()
            val qty = qtyEditText.text.toString().toDoubleOrNull()
            val isBuy = buyRadioButton.isChecked

            if (selectedAsset.isNotEmpty() && price != null && qty != null) {
                val orderType = if (isBuy) "Buy" else "Sell"
                val order = Order(
                    sym = selectedAsset,
                    price = price,
                    qty = qty,
                    type = orderType
                )

                globalUserView.addOrder(order)
                Toast.makeText(this, "Order Submitted: $selectedAsset, $price, $qty, $orderType", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Missing required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}