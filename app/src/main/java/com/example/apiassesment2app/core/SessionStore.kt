package com.example.apiassesment2app.core

import android.content.Context
import androidx.core.content.edit

object SessionStore {
    private const val PREF = "session_prefs"
    private const val KEYPASS = "keypass"

    fun saveKeypass(context: Context, key: String) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit {
            putString(KEYPASS, key)
        }
    }

    fun getKeypass(context: Context): String? =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEYPASS, null)

    fun clear(context: Context) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit { clear() }
    }
}
