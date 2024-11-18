package com.example.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sightsafe.databinding.ActivityLoginUserBinding
import com.example.user.EmailValidator.addEmailValidation
import com.example.user.PasswordValidator.addPasswordValidation


class LoginUser : AppCompatActivity() {
    private lateinit var binding: ActivityLoginUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add email validation
        binding.editTextEmail.addEmailValidation()

        binding.RegisterUser.setOnClickListener {
            val intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Exit the application
    }
}