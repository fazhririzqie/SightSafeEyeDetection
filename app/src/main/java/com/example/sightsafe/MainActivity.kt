package com.example.sightsafe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.sightsafe.databinding.ActivityMainBinding
import com.example.sightsafe.user.LoginUser
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1500)
        installSplashScreen()

        // Initialize binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()


        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // User is not logged in, navigate to LoginUser
            val intent = Intent(this, LoginUser::class.java)
            startActivity(intent)
            finish() // Close MainActivity to prevent returning to splash screen
        } else {
            // User is logged in, proceed to main content
            // User is logged in, display email
            val email = currentUser.email
            binding.userEmail.text = email
        }

        // Set up logout button
        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginUser::class.java)
            startActivity(intent)
            finish() // Close MainActivity to prevent returning to it
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Exit the application.
    }
}