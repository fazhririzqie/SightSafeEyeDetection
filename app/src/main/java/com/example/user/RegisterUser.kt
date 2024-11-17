package com.example.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sightsafe.databinding.ActivityRegisterUserBinding

class RegisterUser : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.LoginUser.setOnClickListener {
            val intent = Intent(this, LoginUser::class.java)
            startActivity(intent)
            finish()
        }

    }
}