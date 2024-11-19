package com.example.sightsafe.user

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

object PasswordValidator {
    private const val MIN_PASSWORD_LENGTH = 8

    fun TextInputEditText.addPasswordValidation() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.length < MIN_PASSWORD_LENGTH) {
                    this@addPasswordValidation.error = "Password less than 8 characters"
                } else {
                    this@addPasswordValidation.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}