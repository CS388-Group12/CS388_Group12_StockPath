package com.example.cs388_group12_stockpath

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
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

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //val appBarConfiguration = AppBarConfiguration(navController.graph)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_charts, R.id.navigation_home, R.id.navigation_news, R.id.navigation_alerts
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        val textTitle=TextView(this).apply {
            text="StockPath"
            textSize=20F
            setTextColor(Color.WHITE)

        }
        val args= Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,Toolbar.LayoutParams.WRAP_CONTENT, Gravity.START)
        toolbar.addView(textTitle,args)
        navView.setupWithNavController(navController)
        val fragmentLabel: TextView = findViewById(R.id.fragment_label)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            fragmentLabel.text=destination.label
            textTitle.text=destination.label
            Log.d("Navigation", "Navigated to ${destination.label}")
        }
    }
}