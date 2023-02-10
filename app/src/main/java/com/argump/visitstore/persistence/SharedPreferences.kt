package com.argump.visitstore.persistence

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {

    private val SESSION_LOGIN = "user_login"
    private val sharedPreferences : SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(SESSION_LOGIN, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun putLoginSession(key: String, value: Boolean) {
        editor.putBoolean(key, value)
            .apply()
    }

    fun getLoginSession(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun putKeepUsername(key: String, value: String) {
        editor.putString(key, value)
            .apply()
    }

    fun getKeepUsername(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun putStore(key: String, value: String) {
        editor.putString(key, value)
            .apply()
    }

    fun getStore(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

}