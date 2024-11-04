package com.example.cineredux_v2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cineredux_v2.models.User

class EditProfileFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Int = 1
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        databaseHelper = DatabaseHelper(requireContext())

        nameEditText = view.findViewById(R.id.editText_name)
        surnameEditText = view.findViewById(R.id.editText_surname)
        usernameEditText = view.findViewById(R.id.editText_username)
        emailEditText = view.findViewById(R.id.editText_email)
        phoneEditText = view.findViewById(R.id.editText_phone)
        passwordEditText = view.findViewById(R.id.editText_password)
        saveButton = view.findViewById(R.id.button_save_profile)

        loadUserProfile()

        saveButton.setOnClickListener {
            saveUserProfile()
        }

        return view
    }

    private fun loadUserProfile() {
        val user = databaseHelper.getUserById(userId)

        if (user != null) {
            nameEditText.setText(user.name)
            surnameEditText.setText(user.surname) // Fixed variable name
            usernameEditText.setText(user.username)
            emailEditText.setText(user.email)
            phoneEditText.setText(user.phone)
            passwordEditText.setText(user.password)
        } else {
            Toast.makeText(requireContext(), "Error loading user profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserProfile() {
        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val username = usernameEditText.text.toString()
        val email = emailEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (name.isBlank() || surname.isBlank() || username.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        val result = databaseHelper.updateUser(userId, name, surname, username, email, phone, password)

        if (result > 0) {
            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
            clearFields()
            navigateToSettings()
        } else {
            Toast.makeText(requireContext(), "Error updating profile", Toast.LENGTH_SHORT).show()
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

    private fun navigateToSettings() {
        requireActivity().supportFragmentManager.popBackStack()
    }
}
