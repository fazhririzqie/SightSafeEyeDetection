package com.example.sightsafe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
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
            // User is logged in, set up BottomNavigationView
            val navController = findNavController(R.id.nav_host_fragment)
            binding.navView.setupWithNavController(navController)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Exit the application.
    }
}