package com.example.sightsafe.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sightsafe.PreferencesHelper
import com.example.sightsafe.R
import com.example.sightsafe.adapter.News
import com.example.sightsafe.adapter.NewsAdapter
import com.example.sightsafe.databinding.ActivityMainBinding
import com.example.sightsafe.getImageUri
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null
    private var email: String? = null

    private var backPressedTime: Long = 0

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
            // User is not logged in, navigate to WelcomeActivity
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish() // Close MainActivity to prevent returning to splash screen
        } else {
            email = currentUser.email
            val name = email?.substringBefore("@")
            binding.textUser.text = "$name"

            // Display check count
            val checkCount = PreferencesHelper.getCheckCount(this, email!!)
            binding.angkaPengecekan.text = checkCount.toString()
        }

        binding.logoutButton.setOnClickListener {
            val profileBottomSheet = ProfileBottomSheet()
            profileBottomSheet.show(supportFragmentManager, "ProfileBottomSheet")
        }

        binding.cardUpload.setOnClickListener {
            val intent = Intent(this, ImageUpload::class.java)
            startActivity(intent)
        }

        binding.selectHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.selectComun.setOnClickListener {
            val bottomSheet = CommunityBottomSheet()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        binding.menu.setOnClickListener {
            val menuBottomSheet = MenuBottomSheet()
            menuBottomSheet.show(supportFragmentManager, "MenuBottomSheet")
        }

        binding.camera.setOnClickListener {
            startCamera()
        }

        // Create a list of News objects
        val newsList = listOf(
            News(
                "Dry eye disease increasing among younger people as research explores link with screen time",
                "ABC",
                R.drawable.abc_cover,
                "https://www.abc.net.au/news/2024-12-06/dry-eye-disease-screen-time-younger-people/104659124"
            ),
            News(
                "What To Know About Vision Loss?",
                "NDTV",
                R.drawable.ndtv_cover,
                "https://www.ndtv.com/health/what-to-know-about-vision-loss-7171042"
            ),
            News(
                "PWM in smartphones and how it can affect your eye health",
                "CNBC",
                R.drawable.cnbc_cover,
                "https://www.cnbctv18.com/technology/for-your-eyes-only-pwm-in-smartphones-and-how-it-can-affect-your-eye-health-19520090.htm"
            )
        )

        // Initialize RecyclerView
        val newsAdapter = NewsAdapter(newsList)
        binding.recyclerViewNews.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewNews.adapter = newsAdapter
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            val intent = Intent(this, ImageUpload::class.java).apply {
                putExtra("imageUri", currentImageUri.toString())
            }
            startActivity(intent)
        } else {
            currentImageUri = null
        }
    }



    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity() // This will close the app
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}