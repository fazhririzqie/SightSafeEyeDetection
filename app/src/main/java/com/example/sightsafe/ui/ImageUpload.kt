package com.example.sightsafe.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.sightsafe.PreferencesHelper
import com.example.sightsafe.R
import com.example.sightsafe.data.FileUploadResponse
import com.example.sightsafe.data.api.ApiConfig
import com.example.sightsafe.databinding.ActivityImageUploadBinding
import com.example.sightsafe.isInternetAvailable
import com.example.sightsafe.reduceFileImage
import com.example.sightsafe.uriToFile
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException

class ImageUpload : AppCompatActivity() {
    private var _binding: ActivityImageUploadBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityImageUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Retrieve and save Firebase ID token
        firebaseAuth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result?.token
                // Save the token to SharedPreferences
                if (idToken != null) {
                    PreferencesHelper.saveAuthToken(this, idToken)
                }
            } else {
                // Handle error -> task.exception
                showToast("Failed to retrieve token: ${task.exception?.message}")
            }
        }

        currentImageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        showImage()

        binding.browse.setOnClickListener { startGallery() }
        binding.analyze.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            showToast("failed to get image")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.selectedImage.visibility = View.VISIBLE
            binding.selectedImage.setImageURI(it)
            binding.selectedImage.background = null // Menghapus background setelah gambar diupload
        }
    }

    private fun uploadImage() {
        if (!isInternetAvailable(this)) {
            showToast("Please turn on your internet connection")
            return
        }

        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image Classification File", "showImage: ${imageFile.path}")

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo", imageFile.name, requestImageFile
            )

            binding.progressBarResult.visibility = View.VISIBLE

            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService(this@ImageUpload)
                    val successResponse = apiService.uploadImage(multipartBody)
                    val resultText = if (successResponse.data.isAboveThreshold == true) {
                        showToast(successResponse.message.toString())
                        String.format("%s with %.2f%%", successResponse.data.result, successResponse.data.confidenceScore)
                    } else {
                        showToast("Model is predicted successfully but under threshold.")
                        String.format("Please use the correct picture because the confidence score is %.2f%%", successResponse.data.confidenceScore)
                    }

                    // Increment check count
                    val email = firebaseAuth.currentUser?.email
                    if (email != null) {
                        PreferencesHelper.incrementCheckCount(this@ImageUpload, email)
                    }

                    binding.progressBarResult.visibility = View.GONE

                    // Tambahkan ini di dalam fungsi uploadImage() setelah berhasil mengunggah gambar
                    val intent = Intent(this@ImageUpload, ResultActivity::class.java).apply {
                        putExtra("imageUri", currentImageUri.toString())
                        putExtra("resultText", resultText)
                    }
                    startActivity(intent)
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    try {
                        val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                        showToast(errorResponse.message.toString())
                    } catch (jsonException: JsonSyntaxException) {
                        showToast("Error: ${errorBody ?: "Please Try Again"}")
                    } finally {
                        // Hide the progress bar in case of an error
                        binding.progressBarResult.visibility = View.GONE
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentImageUri", currentImageUri?.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentImageUri = savedInstanceState.getString("currentImageUri")?.let { Uri.parse(it) }
        showImage()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}