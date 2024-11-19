package com.example.sightsafe.user

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sightsafe.MainActivity
import com.example.sightsafe.R
import com.example.sightsafe.databinding.ActivityLoginUserBinding
import com.example.sightsafe.user.EmailValidator.addEmailValidation
import com.google.firebase.auth.FirebaseAuth


class LoginUser : AppCompatActivity() {
    private lateinit var binding: ActivityLoginUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add email validation
        binding.editTextEmail.addEmailValidation()

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Set initial visibility of eye icon
        binding.togglePasswordVisibility.visibility = ImageView.INVISIBLE

        // Toggle password visibility
        binding.togglePasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(binding.editTextPassword, binding.togglePasswordVisibility, isPasswordVisible)
        }

        // Add TextWatcher to show/hide eye icon
        binding.editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.togglePasswordVisibility.visibility = if (s.isNullOrEmpty()) ImageView.INVISIBLE else ImageView.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.Login.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        if (user != null && user.isEmailVerified) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            showAlertDialog("Login Failed", "Please verify your email before logging in.")
                            firebaseAuth.signOut() // Sign out user if email is not verified
                        }
                    } else {
                        showAlertDialog("Login Failed", "Incorrect email or password.")
                    }
                }
            } else {
                showAlertDialog("Empty Fields", "Email and password fields cannot be empty.")
            }
        }

        binding.RegisterUser.setOnClickListener {
            val intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
        }

    }

    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Exit the application
    }

    private fun togglePasswordVisibility(editText: EditText, imageView: ImageView, isVisible: Boolean) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            imageView.setImageResource(R.drawable.ic_eye_off)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            imageView.setImageResource(R.drawable.ic_eye)
        }
        editText.setSelection(editText.text.length)
    }
}