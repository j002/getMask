package com.app.fr.getmymask.helpers

import android.content.Context
import android.util.Base64
import com.app.fr.getmymask.Constants
import io.reactivex.Single

class SharedPref {

    companion object {

        fun getInt(context: Context, prefKey: String, defaultValue: Int = 0): Single<Int> = Single.create<Int> { emitter ->

            val prefs = getSharedPreferences(context)
            val sharedPreferenceValue = prefs.getInt(prefKey, defaultValue)

            emitter.onSuccess(sharedPreferenceValue)
        }

        fun getString(context: Context, prefKey: String, defaultValue: String = ""): Single<String> = Single.create<String> { emitter ->

            val prefs = getSharedPreferences(context)
            val sharedPreferenceValue = prefs.getString(prefKey, defaultValue)

            emitter.onSuccess(sharedPreferenceValue!!)
        }

        fun getDecryptString(context: Context, prefKey: String, defaultValue: String = ""): Single<String> = Single.create<String> { emitter ->

            val prefs = getSharedPreferences(context)
            val sharedPreferenceValueEncrypt = prefs.getString(prefKey, defaultValue)
            val decryptValue = decrypt(sharedPreferenceValueEncrypt!!)
            emitter.onSuccess(decryptValue)
        }

        fun putInt(context: Context, preferenceKey: String, intValue: Int) {
            val prefs = getSharedPreferences(context)
            prefs.edit().putInt(preferenceKey, intValue).apply()
        }

        fun putString(context: Context, preferenceKey: String, stringValue: String) {
            val prefs = getSharedPreferences(context)
            prefs.edit().putString(preferenceKey, stringValue).apply()
        }

        fun putEncryptString(context: Context, preferenceKey: String, stringValue: String) {
            val prefs = getSharedPreferences(context)
            prefs.edit().putString(preferenceKey, encrypt(stringValue)).apply()
        }

        private fun getSharedPreferences(context: Context) = context.getSharedPreferences(
            Constants.PREFERENCE_KEY, Context.MODE_PRIVATE)

        private fun encrypt(input: String): String {
            // TODO not a real encrypt but make the job for the moment
            return Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)
        }

        private fun decrypt(input: String): String {
            return String(Base64.decode(input, Base64.DEFAULT))
        }
    }
}