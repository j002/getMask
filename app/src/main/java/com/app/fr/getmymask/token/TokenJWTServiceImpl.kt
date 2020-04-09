package com.app.fr.getmymask.token

import android.content.Context
import com.app.fr.getmymask.Constants
import com.app.fr.getmymask.helpers.sharedpreferences.SharedPreferencesHelper

import io.reactivex.Completable
import javax.inject.Inject


class TokenJWTServiceImpl @Inject constructor(
    private val context: Context,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : TokenJWTService {


    override fun saveRefreshToken(refreshToken: String): Completable = Completable.create { emitter ->
        sharedPreferencesHelper.putEncryptString(context, Constants.USER_REFRESH_TOKEN_PREF_KEY, refreshToken)
        emitter.onComplete()
    }

    override fun saveTokenIfValid(token: String): Completable = Completable.create { emitter ->
        sharedPreferencesHelper.putEncryptString(context, Constants.USER_TOKEN_PREF_KEY, token)
        emitter.onComplete()
    }

    override fun getToken(): String {
        return sharedPreferencesHelper.getDecryptString(context, Constants.USER_TOKEN_PREF_KEY).blockingGet()
    }

    override fun getRefreshToken(): String {
        return sharedPreferencesHelper.getDecryptString(context, Constants.USER_REFRESH_TOKEN_PREF_KEY).blockingGet()
    }

    override fun hasTokenValid(): Boolean {
        return getToken().isNotEmpty()
    }

    override fun deleteToken() {
        sharedPreferencesHelper.deleteData(context, Constants.USER_TOKEN_PREF_KEY)
    }
}