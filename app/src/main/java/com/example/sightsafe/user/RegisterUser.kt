package com.example.sightsafe.user

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sightsafe.databinding.ActivityRegisterUserBinding
import com.example.sightsafe.user.EmailValidator.addEmailValidation
import com.example.sightsafe.user.PasswordValidator.addPasswordValidation
import com.google.firebase.auth.FirebaseAuth


class RegisterUser : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add email and password validation
        binding.editTextEmail.addEmailValidation()
        binding.editTextPassword.addPasswordValidation()

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

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

        playAnimation()

    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val texteditpasswordlayout =
            ObjectAnimator.ofFloat(binding.texteditPasswordLayout, View.ALPHA, 1f).setDuration(100)
        val confirmpasswordTextView =
            ObjectAnimator.ofFloat(binding.confirmpasswordTextView, View.ALPHA, 1f).setDuration(100)
        val confirmpasswordEditTextLayout =
            ObjectAnimator.ofFloat(binding.confirmpasswordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val register = ObjectAnimator.ofFloat(binding.Register, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                nameEditTextLayout,
                passwordTextView,
                texteditpasswordlayout,
                confirmpasswordTextView,
                confirmpasswordEditTextLayout,
                register
            )
            startDelay = 100
        }.start()
    }


    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

}