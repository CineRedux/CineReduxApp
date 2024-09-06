package com.example.cineredux

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class registration : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var nameEditText: EditText
    lateinit var surnameEditText: EditText
    lateinit var usernameEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var phoneEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        dbHelper = DatabaseHelper(this)

        nameEditText = findViewById(R.id.name)
        surnameEditText = findViewById(R.id.surname)
        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        phoneEditText = findViewById(R.id.phoneNumber)
        passwordEditText = findViewById(R.id.password)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (name.isNotEmpty() && surname.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()) {
                val result = dbHelper.addUser(name, surname, username, email, phone, password)

                if (result > 0) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                    clearFields()

                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        nameEditText.text.clear()
        surnameEditText.text.clear()
        usernameEditText.text.clear()
        emailEditText.text.clear()
        phoneEditText.text.clear()
        passwordEditText.text.clear()
    }
}
