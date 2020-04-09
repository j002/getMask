package com.app.fr.getmymask.helpers.sharedpreferences

import android.content.Context
import io.reactivex.Single

interface SharedPreferencesHelper {
    fun getInt(context: Context, prefKey: String): Single<Int>

    fun getString(context: Context, prefKey: String): Single<String>

    fun getDecryptString(context: Context, prefKey: String): Single<String>

    fun putInt(context: Context, preferenceKey: String, intValue: Int)

    fun putString(context: Context, preferenceKey: String, stringValue: String)

    fun putEncryptString(context: Context, preferenceKey: String, stringValue: String)

    fun deleteData(context: Context,preferenceKey: String)
}
