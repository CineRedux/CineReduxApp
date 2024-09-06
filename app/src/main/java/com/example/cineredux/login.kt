package com.example.cineredux

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cineredux.registration


class Login : AppCompatActivity() {

    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        databaseHelper = DatabaseHelper(this)


        val usernameField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)


        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val googleSignInButton = findViewById<ImageButton>(R.id.googleSignInButton)


        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            // Verify credentials
            if (databaseHelper.verifyUser(username, password)) {
                // If credentials match, proceed to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {

                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }


        registerButton.setOnClickListener {
            val intent = Intent(this, registration::class.java)
            startActivity(intent)
        }


        googleSignInButton.setOnClickListener {

            Toast.makeText(this, "Google Sign-In clicked", Toast.LENGTH_SHORT).show()

        }
    }
}
