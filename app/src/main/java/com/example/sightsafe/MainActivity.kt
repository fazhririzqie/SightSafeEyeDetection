package com.example.sightsafe

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.sightsafe.databinding.ActivityMainBinding
import com.example.sightsafe.ui.FragmentImageUpload
import com.example.sightsafe.user.CommunityBottomSheet
import com.example.sightsafe.user.WelcomeActivity
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
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish() // Close MainActivity to prevent returning to splash screen
        }else{
            val email = currentUser.email
            val name = email?.substringBefore("@")
            binding.textUser.text = "Hi, $name"
        }
        binding.logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        binding.cardUpload.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main, FragmentImageUpload())
                .addToBackStack(null)
                .commit()
        }

        binding.selectComun.setOnClickListener {
            val bottomSheet = CommunityBottomSheet()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Apakah yakin akan logout?")
            .setPositiveButton("Iya") { dialog, id ->
                firebaseAuth.signOut()
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish() // Close MainActivity to prevent returning to it
            }
            .setNegativeButton("Tidak") { dialog, id ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Exit the application.
    }
}