package com.app.fr.getmymask.helpers.sharedpreferences

import android.content.Context
import android.util.Base64
import com.app.fr.getmymask.Constants
import io.reactivex.Single
import javax.inject.Inject

class SharedPreferencesHelperImpl @Inject constructor() : SharedPreferencesHelper {

    override fun getInt(context: Context, prefKey: String): Single<Int> = Single.create<Int> { emitter ->
        val prefs = getSharedPreferences(context)
        val sharedPreferenceValue = prefs.getInt(prefKey, 0)

        emitter.onSuccess(sharedPreferenceValue)
    }

    override fun getString(context: Context, prefKey: String): Single<String> = Single.create<String> { emitter ->
        val prefs = getSharedPreferences(context)
        val sharedPreferenceValue = prefs.getString(prefKey, "")

        emitter.onSuccess(sharedPreferenceValue!!)
    }

    override fun getDecryptString(context: Context, prefKey: String): Single<String> = Single.create<String> { emitter ->

        val prefs = getSharedPreferences(context)
        val sharedPreferenceValueEncrypt = prefs.getString(prefKey, "")
        val decryptValue = decrypt(sharedPreferenceValueEncrypt!!)
        emitter.onSuccess(decryptValue)
    }

    override fun putInt(context: Context, preferenceKey: String, intValue: Int) {
        val prefs = getSharedPreferences(context)
        prefs.edit().putInt(preferenceKey, intValue).apply()
    }

    override fun putString(context: Context, preferenceKey: String, stringValue: String) {
        val prefs = getSharedPreferences(context)
        prefs.edit().putString(preferenceKey, stringValue).apply()
    }

    override fun putEncryptString(context: Context, preferenceKey: String, stringValue: String) {
        val prefs = getSharedPreferences(context)
        prefs.edit().putString(preferenceKey, encrypt(stringValue)).apply()
    }

    override fun deleteData(context: Context, preferenceKey: String) {
        val prefs = getSharedPreferences(context)
        prefs.edit().remove(preferenceKey).apply()
    }

    private fun getSharedPreferences(context: Context) =
        context.getSharedPreferences(Constants.USER_PREFERENCES_KEY, Context.MODE_PRIVATE)

    private fun encrypt(input: String): String {
        // TODO not a real encrypt but make the job for the moment
        return Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)
    }

    private fun decrypt(input: String): String {
        return String(Base64.decode(input, Base64.DEFAULT))
    }
}
