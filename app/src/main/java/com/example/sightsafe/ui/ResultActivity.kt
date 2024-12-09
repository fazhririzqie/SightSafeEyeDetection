package com.example.sightsafe.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sightsafe.adapter.Result
import com.example.sightsafe.databinding.ActivityResultBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        firebaseAuth = FirebaseAuth.getInstance()
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

        binding.exit.setOnClickListener {
            navigateToHome()
        }
    }

    private fun saveResultToFirebase(imageUri: String, resultText: String?) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val resultId = database.child("users").child(userId).child("results").push().key ?: return
        val result = Result(resultId, imageUri, resultText)

        database.child("users").child(userId).child("results").child(resultId).setValue(result)
            .addOnSuccessListener {
                Toast.makeText(this, "Result saved successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save result", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateToHome()
    }
}