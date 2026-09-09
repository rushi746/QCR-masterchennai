package com.example.newapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PreferenceManager {

    private val sharedPrefName = "CSR_PREF"

    companion object{

        val KEY_ASSET_NO = "key_asset_no"
        private val preferenceManager by lazy { PreferenceManager() }
        private lateinit var context: Context

        fun getInstance(context: Context): PreferenceManager {
            this.context = context
            return preferenceManager
        }

    }

    fun getValue(key: String?): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(
                sharedPrefName,
                Context.MODE_PRIVATE
            )
        return prefs.getString(key, null)
    }

    fun getIntValue(key: String?): Int {
        val prefs: SharedPreferences =
            context.getSharedPreferences(
                sharedPrefName,
                Context.MODE_PRIVATE
            )
        return prefs.getInt(key, 0)
    }

    fun add(key: String?, value: String?): PreferenceManager? {
        return try {
            context.getSharedPreferences(
                sharedPrefName,
                Context.MODE_PRIVATE
            ).edit().putString(key, value).apply()
            preferenceManager
        } catch (ex: Exception) {
            Log.e("Preference", "SharedPrefHandler: AddSharedPref: Exception: " + ex.message, ex)
            throw ex
        }
    }

    fun add(key: String?, value: Int): PreferenceManager? {
        return try {
            context.getSharedPreferences(
                sharedPrefName,
                Context.MODE_PRIVATE
            ).edit().putInt(key, value).apply()
            preferenceManager
        } catch (e: Exception) {
            Log.e("Preference", "SharedPrefHandler: AddSharedPref: Exception: " + e.message, e)
            throw e
        }
    }

    fun remove(key: String?): PreferenceManager? {
        return try {
            context.getSharedPreferences(
                sharedPrefName,
                Context.MODE_PRIVATE
            ).edit().remove(key).apply()
            preferenceManager
        } catch (ex: Exception) {
            Log.e("Pref", "SharedPrefHandler: AddSharedPref: Exception: " + ex.message, ex)
            throw ex
        }
    }

}