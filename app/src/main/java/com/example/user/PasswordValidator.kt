package com.example.user

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

object PasswordValidator {
    private const val MAX_PASSWORD_LENGTH = 8

    fun TextInputEditText.addPasswordValidation() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.length > MAX_PASSWORD_LENGTH) {
                    this@addPasswordValidation.error = "Password must be at most 8 characters"
                } else {
                    this@addPasswordValidation.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}