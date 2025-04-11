package com.example.cs388_group12_stockpath

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cs388_group12_stockpath.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val globalUserView: GlobalUserView by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        globalUserView.uid.observe(this) {
            Log.d("MainActivity", "User ID: $it")
        }
        globalUserView.email.observe(this) {
            Log.d("MainActivity", "Email: $it")
        }

        val userEmailText: TextView = findViewById(R.id.user_email_text)
        val authButton: Button = findViewById(R.id.auth_button)
        globalUserView.uid.observe(this) { uid ->
            globalUserView.email.observe(this) { email ->
                if (uid == "Guest") {
                    userEmailText.text = "Welcome: Guest"
                    authButton.text = "Sign In"
                    authButton.setOnClickListener {
                        val intent = Intent(this, RegisterActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    userEmailText.text = "Welcome: $email"
                    authButton.text = "Sign Out"
                    authButton.setOnClickListener {
                        FirebaseAuth.getInstance().signOut()
                        globalUserView.refreshUser()
                    }
                }
            }
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //Dissable auto title
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //val appBarConfiguration = AppBarConfiguration(navController.graph)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_charts, R.id.navigation_home, R.id.navigation_news, R.id.navigation_alerts
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
//        val textTitle=TextView(this).apply {
//            text="StockPath"
//            textSize=20F
//            setTextColor(Color.WHITE)
//
//        }
//        val args= Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,Toolbar.LayoutParams.WRAP_CONTENT, Gravity.START)
//        toolbar.addView(textTitle,args)
        navView.setupWithNavController(navController)
        val fragmentLabel: TextView = findViewById(R.id.fragment_label)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            //textTitle.text=destination.label
            fragmentLabel.text=destination.label
            Log.d("Navigation", "Navigated to ${destination.label}")
            when (destination.id) {
                R.id.navigation_home, R.id.navigation_charts, R.id.navigation_news, R.id.navigation_alerts -> {
                    toolbar.visibility = View.VISIBLE
                }
                else -> {
                    //toolbar.visibility = View.GONE
                    toolbar.visibility = View.VISIBLE
                }
            }
        }
    }
}