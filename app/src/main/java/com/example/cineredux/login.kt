// login.kt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cineredux.DatabaseHelper
import com.example.cineredux.MainActivity
import com.example.cineredux.R
import com.example.cineredux.registration

class login : AppCompatActivity() {

    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize the database helper
        databaseHelper = DatabaseHelper(this)

        // Get references to the username and password fields
        val usernameField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)

        // Get references to the login, register, and Google Sign-In buttons
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val googleSignInButton = findViewById<ImageButton>(R.id.googleSignInButton)

        // Set an OnClickListener for the login button
        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            // Verify credentials
            if (databaseHelper.verifyUser(username, password)) {
                // If credentials match, proceed to MainActivity
                val intent = Intent(this@login, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // If credentials don't match, show an error
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        // Set an OnClickListener for the register button
        registerButton.setOnClickListener {
            // Redirect to the registration page
            val intent = Intent(this@login, registration::class.java)
            startActivity(intent)
        }

        // Set an OnClickListener for the Google Sign-In button
        googleSignInButton.setOnClickListener {
            // Handle Google Sign-In logic here
            Toast.makeText(this, "Google Sign-In clicked", Toast.LENGTH_SHORT).show()
            // TODO: Implement Google Sign-In functionality
        }
    }
}
