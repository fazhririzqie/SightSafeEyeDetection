package com.example.sightsafe.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sightsafe.R
import com.example.sightsafe.adapter.Result
import com.example.sightsafe.databinding.ActivityResultBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Ambil data dari Intent
        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        val resultText = intent.getStringExtra("resultText")

        // Tampilkan data
        imageUri?.let {
            binding.selectedImage.setImageURI(it)
        }
        binding.resultTextView.text = resultText

        // Simpan data ke Firebase ketika tombol save diklik
        binding.save.setOnClickListener {
            saveResultToFirebase(imageUri.toString(), resultText)
        }
    }

    private fun saveResultToFirebase(imageUri: String, resultText: String?) {
        val resultId = database.push().key ?: return
        val result = Result(resultId, imageUri, resultText)

        database.child("results").child(resultId).setValue(result)
            .addOnSuccessListener {
                Toast.makeText(this, "Result saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save result", Toast.LENGTH_SHORT).show()
            }
    }
}
