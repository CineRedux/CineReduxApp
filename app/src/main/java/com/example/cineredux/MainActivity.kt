package com.example.cineredux

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // Check if user is logged in (you can implement this check based on shared preferences or database)
//        val isLoggedIn = false // Change this logic to check login status
//
//        if (isLoggedIn) {
//            // Navigate to the main activity of your app
//            setContentView(R.layout.activity_main)
//        } else {
//            // If user is not logged in, navigate to Login activity
//            val intent = Intent(this, Login::class.java)
//            startActivity(intent)
//            finish() // Optional: Prevent going back to this activity
//        }
    }

}
