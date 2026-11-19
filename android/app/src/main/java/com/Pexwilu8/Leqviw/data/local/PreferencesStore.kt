package com.Pexwilu8.Leqviw.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int): Int = runCatching { prefs.getInt(key, fallback) }.getOrDefault(fallback)

    fun bool(key: String, fallback: Boolean): Boolean = runCatching { prefs.getBoolean(key, fallback) }.getOrDefault(fallback)

    fun text(key: String, fallback: String): String = runCatching { prefs.getString(key, fallback) ?: fallback }.getOrDefault(fallback)

    fun intList(key: String, size: Int): List<Int> {
        val raw = text(key, "")
        if (raw.isBlank()) return List(size) { 0 }
        val parsed = raw.split(SEPARATOR).mapNotNull { it.trim().toIntOrNull() }
        return List(size) { parsed.getOrElse(it) { 0 } }
    }

    fun textSet(key: String): Set<String> {
        val raw = text(key, "")
        if (raw.isBlank()) return emptySet()
        return raw.split(SEPARATOR).map { it.trim() }.filter { it.isNotEmpty() }.toSet()
    }

    fun write(block: SharedPreferences.Editor.() -> Unit) {
        val editor = prefs.edit()
        editor.block()
        editor.apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        const val SEPARATOR = ","
        private const val STORE_NAME = "velvet_gem_heist"

        fun join(values: List<Int>): String = values.joinToString(SEPARATOR)

        fun join(values: Set<String>): String = values.joinToString(SEPARATOR)
    }
}
