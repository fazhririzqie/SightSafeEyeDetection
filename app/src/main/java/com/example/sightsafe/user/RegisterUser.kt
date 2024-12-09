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
        val imageViewAnim = ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

    val titleAnim = ObjectAnimator.ofFloat(binding.titleTextView, View.TRANSLATION_X, -1000f, 0f).setDuration(150)
    val emailTextViewAnim = ObjectAnimator.ofFloat(binding.emailTextView, View.TRANSLATION_X, 1000f, 0f).setDuration(150)
    val nameEditTextLayoutAnim = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.TRANSLATION_X, -1000f, 0f).setDuration(150)
    val passwordTextViewAnim = ObjectAnimator.ofFloat(binding.passwordTextView, View.TRANSLATION_X, 1000f, 0f).setDuration(150)
    val texteditPasswordLayoutAnim = ObjectAnimator.ofFloat(binding.texteditPasswordLayout, View.TRANSLATION_X, -1000f, 0f).setDuration(150)
    val confirmpasswordTextViewAnim = ObjectAnimator.ofFloat(binding.confirmpasswordTextView, View.TRANSLATION_X, 1000f, 0f).setDuration(150)
    val confirmpasswordEditTextLayoutAnim = ObjectAnimator.ofFloat(binding.confirmpasswordEditTextLayout, View.TRANSLATION_X, -1000f, 0f).setDuration(150)
    val registerAnim = ObjectAnimator.ofFloat(binding.Register, View.TRANSLATION_X, 1000f, 0f).setDuration(150)

    val titleFadeIn = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 0f, 1f).setDuration(150)
    val emailTextViewFadeIn = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(150)
    val nameEditTextLayoutFadeIn = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 0f, 1f).setDuration(150)
    val passwordTextViewFadeIn = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(150)
    val texteditPasswordLayoutFadeIn = ObjectAnimator.ofFloat(binding.texteditPasswordLayout, View.ALPHA, 0f, 1f).setDuration(150)
    val confirmpasswordTextViewFadeIn = ObjectAnimator.ofFloat(binding.confirmpasswordTextView, View.ALPHA, 0f, 1f).setDuration(150)
    val confirmpasswordEditTextLayoutFadeIn = ObjectAnimator.ofFloat(binding.confirmpasswordEditTextLayout, View.ALPHA, 0f, 1f).setDuration(150)
    val registerFadeIn = ObjectAnimator.ofFloat(binding.Register, View.ALPHA, 0f, 1f).setDuration(150)

    AnimatorSet().apply {
        playSequentially(
            AnimatorSet().apply { playTogether(titleAnim, titleFadeIn) },
            AnimatorSet().apply { playTogether(emailTextViewAnim, emailTextViewFadeIn) },
            AnimatorSet().apply { playTogether(nameEditTextLayoutAnim, nameEditTextLayoutFadeIn) },
            AnimatorSet().apply { playTogether(passwordTextViewAnim, passwordTextViewFadeIn) },
            AnimatorSet().apply { playTogether(texteditPasswordLayoutAnim, texteditPasswordLayoutFadeIn) },
            AnimatorSet().apply { playTogether(confirmpasswordTextViewAnim, confirmpasswordTextViewFadeIn) },
            AnimatorSet().apply { playTogether(confirmpasswordEditTextLayoutAnim, confirmpasswordEditTextLayoutFadeIn) },
            AnimatorSet().apply { playTogether(registerAnim, registerFadeIn) }
        )
        startDelay = 100
    }.start()

    imageViewAnim.start()
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