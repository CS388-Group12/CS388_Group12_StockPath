package com.example.cs388_group12_stockpath

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
//        globalUserView.uid.observe(this) {
//            Log.d("MainActivity", "User ID: $it")
//        }
//        globalUserView.email.observe(this) {
//            Log.d("MainActivity", "Email: $it")
//        }
        val user_email_text: TextView = findViewById(R.id.user_email_text)
        val auth_Button: Button = findViewById(R.id.auth_button)

        globalUserView.user.observe(this) { user ->
            if (user != null) {
                user_email_text.text = "Welcome: ${user.email}"
                auth_Button.text = "Sign Out"
                auth_Button.setOnClickListener {
                    FirebaseAuth.getInstance().signOut()
                    globalUserView.refreshUser()
                }
            } else {
                user_email_text.text = "Welcome: Guest"
                auth_Button.text = "Sign In"
                auth_Button.setOnClickListener {
                    // Navigate to sign-in activity
                    startActivity(Intent(this, RegisterActivity::class.java))
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
//
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