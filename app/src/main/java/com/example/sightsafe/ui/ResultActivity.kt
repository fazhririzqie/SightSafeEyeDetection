package com.example.sightsafe.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sightsafe.R
import com.example.sightsafe.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        val resultText = intent.getStringExtra("resultText")

        // Tampilkan data
        imageUri?.let {
            binding.selectedImage.setImageURI(it)
        }
        binding.resultTextView.text = resultText

    }
}