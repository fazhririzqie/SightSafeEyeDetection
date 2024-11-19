package com.example.sightsafe.user

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sightsafe.R
import com.example.sightsafe.databinding.ActivityRegisterUserBinding
import com.example.sightsafe.user.EmailValidator.addEmailValidation
import com.example.sightsafe.user.PasswordValidator.addPasswordValidation
import com.google.firebase.auth.FirebaseAuth


class RegisterUser : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add email and password validation
        binding.editTextEmail.addEmailValidation()
        binding.editTextPassword.addPasswordValidation()

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Set initial visibility of eye icons
        binding.togglePasswordVisibility.visibility = ImageView.INVISIBLE
        binding.toggleConfirmPasswordVisibility.visibility = ImageView.INVISIBLE

        // Toggle password visibility
        binding.togglePasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(binding.editTextPassword, binding.togglePasswordVisibility, isPasswordVisible)
        }

        // Toggle confirm password visibility
        binding.toggleConfirmPasswordVisibility.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            togglePasswordVisibility(binding.editTextConfirmPassword, binding.toggleConfirmPasswordVisibility, isConfirmPasswordVisible)
        }

        // Add TextWatcher to show/hide eye icon
        binding.editTextPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.togglePasswordVisibility.visibility = if (s.isNullOrEmpty()) ImageView.INVISIBLE else ImageView.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.editTextConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.toggleConfirmPasswordVisibility.visibility = if (s.isNullOrEmpty()) ImageView.INVISIBLE else ImageView.VISIBLE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.Register.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confirmPass = binding.editTextConfirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (password == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Send verification email
                            firebaseAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Registration successful. Please verify your email.", Toast.LENGTH_SHORT).show()
                                    firebaseAuth.signOut() // Sign out user after registration
                                    val intent = Intent(this, LoginUser::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    showAlertDialog("Error", "Failed to send verification email.")
                                }
                            }
                        } else {
                            showAlertDialog("Registration Failed", "Email already in use. Please use a different email.")
                        }
                    }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.LoginUser.setOnClickListener {
            val intent = Intent(this, LoginUser::class.java)
            startActivity(intent)
            finish()
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