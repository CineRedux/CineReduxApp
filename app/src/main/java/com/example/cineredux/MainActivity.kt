package com.example.cineredux

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set default fragment when activity is created
        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), false)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment(), true)
                    true
                }
                R.id.nav_search -> {
                    loadFragment(SearchFragment(), true)
                    true
                }
                R.id.nav_watchlist -> {
                    loadFragment(WatchlistFragment(), true)
                    true
                }
                R.id.nav_settings -> {
                    loadFragment(SettingsFragment(), true)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, animate: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()

        if (animate) {
            // Add custom animations for fragment transitions
            transaction.setCustomAnimations(
                R.anim.enter_from_right, // Enter animation
                R.anim.exit_to_left,     // Exit animation
                R.anim.enter_from_left,  // Pop enter animation (when back pressed)
                R.anim.exit_to_right     // Pop exit animation
            )
        }

        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
