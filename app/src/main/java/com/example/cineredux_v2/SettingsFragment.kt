package com.example.cineredux_v2

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import android.graphics.Color
import androidx.core.content.ContextCompat
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.Locale
import com.example.cineredux_v2.utils.LocaleHelper


class SettingsFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Int = 1 // Assuming you're retrieving the logged-in user's ID
    private lateinit var btnLightMode: Button
    private lateinit var btnDarkMode: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize DatabaseHelper
        databaseHelper = DatabaseHelper(requireContext())

        val editProfileButton: Button = view.findViewById(R.id.btn_edit_profile)
        val deleteProfileButton: Button = view.findViewById(R.id.btn_delete_profile)
        val logoutButton: Button = view.findViewById(R.id.btn_logout)
        val languageButton: Button = view.findViewById(R.id.btn_language)
        btnLightMode = view.findViewById(R.id.btn_light_mode)
        btnDarkMode = view.findViewById(R.id.btn_dark_mode)

        // Set button texts
        editProfileButton.text = getString(R.string.edit_profile)
        deleteProfileButton.text = getString(R.string.delete_profile)
        logoutButton.text = getString(R.string.logout)
        languageButton.text = getString(R.string.language)
        btnLightMode.text = getString(R.string.light_mode)
        btnDarkMode.text = getString(R.string.dark_mode)

        // Set up Edit Profile Button
        editProfileButton.setOnClickListener {
            // Replace the fragment manually
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, EditProfileFragment())
            transaction.addToBackStack(null) // Optional: allows back navigation
            transaction.commit()
        }

        // Set up Delete Profile Button
        deleteProfileButton.setOnClickListener {
            // Show confirmation dialog
            showDeleteConfirmationDialog()
        }

        // Set up Logout Button
        logoutButton.setOnClickListener {
            // Show confirmation dialog for logout
            showLogoutConfirmationDialog()
        }

        // Set up Language Button
        languageButton.setOnClickListener {
            showLanguageSelectionDialog()
        }

        return view
    }

    // Function to show delete confirmation dialog
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_profile_title))
            .setMessage(getString(R.string.delete_profile_message))
            .setPositiveButton(getString(R.string.delete_profile)) { _, _ -> deleteProfile() }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    // Function to delete the profile
    private fun deleteProfile() {
        // Assuming userId is the ID of the logged-in user
        val rowsDeleted = databaseHelper.deleteUser(userId)

        if (rowsDeleted > 0) {
            Toast.makeText(requireContext(), "Profile deleted successfully", Toast.LENGTH_SHORT).show()
            // Optionally, redirect to a login or welcome screen after deletion
            val intent = Intent(requireContext(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Error deleting profile", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to show logout confirmation dialog
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.logout_title))
            .setMessage(getString(R.string.logout_message))
            .setPositiveButton(getString(R.string.logout)) { _, _ -> logout() }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    // Function to handle logout
    private fun logout() {

        databaseHelper.clearUserSession() // Implement this method to clear user data

        // Redirect to the login screen
        val intent = Intent(requireContext(), Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the background color based on the theme
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fragmentBackground))

        btnLightMode  = view.findViewById(R.id.btn_light_mode)
        btnDarkMode = view.findViewById(R.id.btn_dark_mode)

        // Load saved theme preference
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)

        // Set initial button states based on saved preference
        updateButtonStates(isDarkMode)

        // Handle Light Mode button click
        btnLightMode.setOnClickListener {
            setLightMode(sharedPreferences)
        }

        // Handle Dark Mode button click
        btnDarkMode.setOnClickListener {
            setDarkMode(sharedPreferences)
        }
    }

    private fun setLightMode(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putBoolean("dark_mode", false).apply()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requireActivity().recreate() // Recreate activity to apply changes
    }

    private fun setDarkMode(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putBoolean("dark_mode", true).apply()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        requireActivity().recreate() // Recreate activity to apply changes
    }


    private fun updateButtonStates(isDarkMode: Boolean) {
        if (isDarkMode) {
            btnLightMode.setBackgroundColor(Color.GRAY)  // Change color to indicate inactive
            btnDarkMode.setBackgroundColor(Color.GRAY)   // Change color to indicate active
        } else {
            btnLightMode.setBackgroundColor(Color.GRAY)   // Change color to indicate active
            btnDarkMode.setBackgroundColor(Color.GRAY)     // Change color to indicate inactive
        }
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf(getString(R.string.english), getString(R.string.afrikaans))
        val languageCodes = arrayOf("en", "af")
        
        val currentLang = LocaleHelper.getLanguage(requireContext())
        val currentIndex = languageCodes.indexOf(currentLang)
        
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.select_language))
            .setSingleChoiceItems(languages, currentIndex) { dialog, which ->
                if (languageCodes[which] != currentLang) {
                    LocaleHelper.setLocale(requireContext(), languageCodes[which])
                    
                    // Restart the entire application
                    requireActivity().packageManager
                        .getLaunchIntentForPackage(requireActivity().packageName)?.let { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
