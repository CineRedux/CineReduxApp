package com.example.cineredux_v2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.cineredux_v2.utils.LocaleHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.util.concurrent.Executor

class Login : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var biometricManager: BiometricManager
    private lateinit var executor: Executor
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val WEB_CLIENT_ID = "644847504028-hcdemgucrvc42q5jptkm3pa0gacinu7v.apps.googleusercontent.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply saved locale
        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this))
        
        setContentView(R.layout.activity_login)

        databaseHelper = DatabaseHelper(this)

        val usernameField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val googleSignInButton = findViewById<ImageButton>(R.id.googleSignInButton)
        val biometricLoginButton = findViewById<ImageButton>(R.id.biometricLoginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // Update hints and button texts with localized strings
        usernameField.hint = getString(R.string.username)
        passwordField.hint = getString(R.string.password)
        loginButton.text = getString(R.string.login_button)
        registerButton.text = getString(R.string.register_button)

        // Set up the Google Sign-In options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID) // Use the constant directly
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        // Set up the executor and biometric prompt
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = createBiometricPrompt()

        biometricLoginButton.setOnClickListener {
            if (canAuthenticateWithBiometrics()) {
                showBiometricPrompt()
            } else {
                Toast.makeText(this, getString(R.string.biometric_unavailable), Toast.LENGTH_SHORT).show()
            }
        }

        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (databaseHelper.verifyUser(username, password)) {
                // Successful login
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            // Navigate to Registration Activity (Assuming you have a Register activity)
            startActivity(Intent(this, registration::class.java))
        }
    }

    // Function to initiate Google Sign-In
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    // Handle the Google Sign-In result
    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            // Successful Google Sign-In, navigate to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } catch (e: ApiException) {
            // Sign-in failed, handle the error
            Toast.makeText(this, getString(R.string.google_signin_failed, e.message), Toast.LENGTH_SHORT).show()
        }
    }

    // Biometric Prompt setup
    private fun createBiometricPrompt(): BiometricPrompt {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_login))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()

        return BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@Login, getString(R.string.auth_error, errString), Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Successful biometric login
                startActivity(Intent(this@Login, MainActivity::class.java))
                finish()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@Login, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun canAuthenticateWithBiometrics(): Boolean {
        biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    private fun showBiometricPrompt() {
        biometricPrompt.authenticate(
            BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.biometric_login))
                .setSubtitle(getString(R.string.biometric_subtitle))
                .setNegativeButtonText(getString(R.string.cancel))
                .build()
        )
    }
}
