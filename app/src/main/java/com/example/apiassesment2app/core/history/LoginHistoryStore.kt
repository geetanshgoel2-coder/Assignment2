package com.example.apiassesment2app.core.history

import android.content.Context
import androidx.core.content.edit

class LoginHistoryStore(private val context: Context) {
    private val prefs = context.getSharedPreferences("login_history", Context.MODE_PRIVATE)

    fun add(event: LoginEvent) {
        val old = prefs.getString("history", "") ?: ""
        val line = "${event.username},${event.timestamp}"
        val updated = if (old.isEmpty()) line else "$old|$line"
        prefs.edit { putString("history", updated) }
    }

    fun all(): List<LoginEvent> {
        val raw = prefs.getString("history", "") ?: return emptyList()
        if (raw.isEmpty()) return emptyList()
        return raw.split("|").mapNotNull {
            val p = it.split(",")
            if (p.size == 2) LoginEvent(p[0], p[1].toLongOrNull() ?: 0L) else null
        }
    }
}
