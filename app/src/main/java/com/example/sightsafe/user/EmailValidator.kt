package com.example.sightsafe.user

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

object EmailValidator {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun TextInputEditText.addEmailValidation() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                if (!isValidEmail(email)) {
                    this@addEmailValidation.error = "Invalid email"
                } else {
                    this@addEmailValidation.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}