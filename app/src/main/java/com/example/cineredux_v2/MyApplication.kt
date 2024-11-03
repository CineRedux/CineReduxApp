package com.example.cineredux_v2

import android.app.Application
import android.content.Context
import com.example.cineredux_v2.utils.LocaleHelper

class MyApplication : Application() {
    override fun attachBaseContext(base: Context) {
        val prefs = base.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = prefs.getString("Selected_Language", "en") ?: "en"
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    override fun onCreate() {
        super.onCreate()
    }
} 