package com.example.sightsafe

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREFS_NAME = "sightsafe_prefs"
    private const val KEY_AUTH_TOKEN = "auth_token"

    private fun getKeyForUser(email: String): String {
        return "check_count_$email"
    }

    fun saveAuthToken(context: Context, token: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_AUTH_TOKEN, token)
        editor.apply()
    }

    fun getAuthToken(context: Context): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_AUTH_TOKEN, null)
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