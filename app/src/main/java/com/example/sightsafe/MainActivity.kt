package com.example.sightsafe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.user.LoginUser


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1500)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        // Pindah ke halaman login setelah splash screen
        val intent = Intent(this, LoginUser::class.java)
        startActivity(intent)
        finish() // Tutup MainActivity agar tidak kembali ke splash screen

    }
}