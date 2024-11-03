package com.example.cineredux_v2.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

object LocaleHelper {
    fun setLocale(context: Context, languageCode: String) {
        val locale = when (languageCode) {
            "af" -> Locale("af", "ZA")
            "en" -> Locale("en")
            "zu" -> Locale("zu", "ZA")
            else -> Locale(languageCode)
        }
        
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        
        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)
        
        // Save the selected language code to SharedPreferences
        val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("language", languageCode).apply()
    }

    fun getLanguage(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language", "en") ?: "en"
    }

    fun onAttach(context: Context): Context {
        val lang = getLanguage(context)
        return createContextWithNewLocale(context, lang)
    }

    private fun createContextWithNewLocale(context: Context, language: String): Context {
        val locale = when (language) {
            "af" -> Locale("af", "ZA")
            "en" -> Locale("en")
            "zu" -> Locale("zu", "ZA")
            else -> Locale(language)
        }
        
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        
        return context.createConfigurationContext(configuration)
    }
} 