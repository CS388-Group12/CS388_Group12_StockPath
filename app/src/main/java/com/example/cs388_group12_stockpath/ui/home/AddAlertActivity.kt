package com.example.cs388_group12_stockpath.ui.home
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cs388_group12_stockpath.GlobalUserView
import com.example.cs388_group12_stockpath.RegisterActivity
import com.example.cs388_group12_stockpath.Alert
import com.example.cs388_group12_stockpath.R
import com.example.cs388_group12_stockpath.StockFuncs

class AddAlertActivity : AppCompatActivity() {

    private val stockFuncs = StockFuncs()
    private val globalUserView: GlobalUserView by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_alert)

        val user_email_text: TextView = findViewById(R.id.user_email_text)
        val auth_button: Button = findViewById(R.id.auth_button)

        val searchEditText: EditText = findViewById(R.id.editTextSearchAsset)
        val searchButton: Button = findViewById(R.id.buttonSearch)
        val resultsListView: ListView = findViewById(R.id.listViewResults)
        val selectedAssetTextView: TextView = findViewById(R.id.textViewSelectedAsset)
        val priceEditText: EditText = findViewById(R.id.editTextPrice)
        val aboveRadioButton: RadioButton = findViewById(R.id.radioAbove)
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

        resultsListView.setOnItemClickListener { _, _, position, _ ->
            val selectedAsset = resultsListView.getItemAtPosition(position).toString()
            selectedAssetTextView.text = selectedAsset
        }

        submitButton.setOnClickListener {
            val selectedAsset = selectedAssetTextView.text.toString()
            val price = priceEditText.text.toString().toDoubleOrNull()
            val isAbove = aboveRadioButton.isChecked
        
            if (selectedAsset.isNotEmpty() && price != null) {
                val watchType = if (isAbove) "above" else "below"
                val alert = Alert(
                    sym = selectedAsset,
                    watchPrice = price,
                    currentPrice = 0.0, //Placeholder for live price
                    watchType = watchType,
                    uid = globalUserView.uid.value ?: "Guest",
                    aid = System.currentTimeMillis().toString(), //Unique ID for the alert
                    timestamp = com.google.firebase.Timestamp.now()
                )
        
                // Upload alert to Firestore
                val uid = globalUserView.uid.value ?: return@setOnClickListener
                val db = FirebaseFirestore.getInstance()
                val alertsRef = db.collection("Users").document(uid).collection("Alerts")
        
                alertsRef.document(alert.aid).set(alert)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Alert Created: $selectedAsset at $price ($watchType)", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("AddAlertActivity", "Error creating alert", exception)
                        Toast.makeText(this, "Failed to create alert", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Missing required fields", Toast.LENGTH_SHORT).show()
            }
        }

    }

}