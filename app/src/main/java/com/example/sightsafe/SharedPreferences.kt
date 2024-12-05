package com.example.sightsafe

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREFS_NAME = "sightsafe_prefs"

    private fun getKeyForUser(email: String): String {
        return "check_count_$email"
    }

    fun getCheckCount(context: Context, email: String): Int {
        val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return preferences.getInt(getKeyForUser(email), 0)
    }

    fun incrementCheckCount(context: Context, email: String) {
        val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        val currentCount = getCheckCount(context, email)
        editor.putInt(getKeyForUser(email), currentCount + 1)
        editor.apply()
    }
}