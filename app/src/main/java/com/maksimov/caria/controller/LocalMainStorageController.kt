package com.maksimov.caria.controller

import android.content.SharedPreferences
import android.webkit.WebBackForwardList
import com.google.gson.Gson
import com.maksimov.caria.data.BrowserHistory
import com.maksimov.caria.data.User

class LocalMainStorageController(private val preferences: SharedPreferences) {

    fun getSettings(): User? {
        return try {
            val json = preferences.getString(USER_PROFILE, null)
            if (json != null) {
                Gson().fromJson(json, User::class.java)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveSettings(
        user: User,
        webBackForwardList: WebBackForwardList?
    ) {
        val history = ArrayList<String>()

        val savedUser = if (webBackForwardList != null) {
            for (index in 0 until webBackForwardList.size) {
                val item = webBackForwardList.getItemAtIndex(index)

                history.add(item.url)
            }
            user.copy(history = BrowserHistory(history = history))
        } else {
            user
        }

        preferences.edit().apply {
            putString(USER_PROFILE, Gson().toJson(savedUser))
        }.apply()
    }

    fun privacyAccepted(isAccepted: Boolean) {
        preferences.edit().apply {
            putBoolean(PRIVACY, isAccepted)
        }.apply()
    }

    fun getPrivacy(): Boolean {
        return preferences.getBoolean(PRIVACY, false)
    }

    companion object {
        private const val USER_PROFILE = "user_profile"
        private const val PRIVACY = "privacy"
        const val APP_STORAGE = "app_storage"
    }
}