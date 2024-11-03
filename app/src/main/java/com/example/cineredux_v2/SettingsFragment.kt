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
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import java.util.Locale
import com.example.cineredux_v2.utils.LocaleHelper
import com.example.cineredux_v2.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Int = 1
    private lateinit var btnLightMode: Button
    private lateinit var btnDarkMode: Button
    private lateinit var languageSpinner: Spinner
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize DatabaseHelper
        databaseHelper = DatabaseHelper(requireContext())

        // Initialize all views
        val editProfileButton: Button = binding.btnEditProfile
        val deleteProfileButton: Button = binding.btnDeleteProfile
        val logoutButton: Button = binding.btnLogout
        btnLightMode = binding.btnLightMode
        btnDarkMode = binding.btnDarkMode
        languageSpinner = binding.languageSpinner

        // Set button texts
        editProfileButton.text = getString(R.string.edit_profile)
        deleteProfileButton.text = getString(R.string.delete_profile)
        logoutButton.text = getString(R.string.logout)
        btnLightMode.text = getString(R.string.light_mode)
        btnDarkMode.text = getString(R.string.dark_mode)

        // Set up button click listeners
        editProfileButton.setOnClickListener {
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, EditProfileFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        deleteProfileButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        Log.d("SettingsFragmentOnCreate", "BeforeLocale: ${resources.configuration.locales[0]}")
        setupLanguageSpinner()
        Log.d("SettingsFragmentOnCreate", "AfterLocale: ${resources.configuration.locales[0]}")

        // Set the background color based on the theme
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.fragmentBackground))

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

    private fun setupLanguageSpinner() {
        // Load saved language preference
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val savedLanguage = sharedPreferences.getString("selected_language", "en")
        
        // Map display names to locale codes
        val languageLocaleMap = mapOf(
            "English" to "en",
            "IsiZulu" to "zu",
            "Afrikaans (ZA)" to "af"
        )
        
        // Map locale codes to display names (for setting initial selection)
        val localeLanguageMap = mapOf(
            "en" to "English",
            "zu" to "IsiZulu",
            "af" to "Afrikaans (ZA)"
        )

        // Set up the language options adapter
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.languages_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            languageSpinner.adapter = adapter
        }

        // Set saved language selection in spinner
        val languages = resources.getStringArray(R.array.languages_array)
        val position = languages.indexOf(localeLanguageMap[savedLanguage])
        if (position >= 0) {
            languageSpinner.setSelection(position)
        }

        // Listen for language selection
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = languages[position]
                val selectedLocaleCode = languageLocaleMap[selectedLanguage] ?: return

                if (selectedLocaleCode != savedLanguage) {
                    Log.d("SettingsFragment", "Selected language: $selectedLanguage, code: $selectedLocaleCode")
                    
                    // Save the selected language code
                    sharedPreferences.edit()
                        .putString("selected_language", selectedLocaleCode)
                        .apply()
                    
                    // Create and set the new locale
                    val locale = when (selectedLocaleCode) {
                        "af" -> Locale("af", "ZA")
                        "zu" -> Locale("zu", "ZA")  // Explicitly set Zulu locale
                        else -> Locale(selectedLocaleCode)
                    }

                    // Update the app's locale
                    Locale.setDefault(locale)
                    val config = resources.configuration
                    config.setLocale(locale)
                    resources.updateConfiguration(config, resources.displayMetrics)

                    // Update LocaleHelper
                    LocaleHelper.setLocale(requireContext(), selectedLocaleCode)

                    // Recreate the entire activity to ensure all strings are updated
                    val intent = requireActivity().intent
                    requireActivity().finish()
                    startActivity(intent)
                    
                    Toast.makeText(
                        requireContext(), 
                        getString(R.string.language_updated, selectedLanguage), 
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
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
            Toast.makeText(requireContext(), getString(R.string.profile_deleted), Toast.LENGTH_SHORT).show()
            // Optionally, redirect to a login or welcome screen after deletion
            val intent = Intent(requireContext(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.error_deleting_profile), Toast.LENGTH_SHORT).show()
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
        Toast.makeText(requireContext(), getString(R.string.logged_out), Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}