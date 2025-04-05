package com.example.cs388_group12_stockpath

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        FirebaseApp.initializeApp(this)

        // Get Authentication instance
        val auth = FirebaseAuth.getInstance()
        // retrieve username and password
        val editTextEmailAddress: EditText = findViewById(R.id.editTextTextEmailAddress)
        val editTextPassword: EditText = findViewById(R.id.editTextTextPassword)
        val buttonRegister: Button = findViewById(R.id.register)
        val buttonLogin: Button = findViewById(R.id.login)
        buttonRegister.setOnClickListener {
            val email = editTextEmailAddress.text.toString()
            val password = editTextPassword.text.toString()
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Creating new user failed", Toast.LENGTH_LONG).show()
            }
        }

        buttonLogin.setOnClickListener {
    val email=editTextEmailAddress.text.toString()
    val password=editTextPassword.text.toString()
    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
        if(task.isSuccessful){
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }.addOnFailureListener { exception ->
//Toast.makeText(this, "Creating new user failed", Toast.LENGTH_LONG).show()
        Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
    }
    }
    }
}