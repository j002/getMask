/*
 * Copyright (c) 2020 by Appndigital, Inc.
 * All Rights Reserved
 */

package com.app.fr.getmymask.api.authentification

import android.content.Context
import android.util.Log
import com.app.fr.getmymask.Constants
import com.app.fr.getmymask.api.helpers.EmitterErrorAdapter
import com.app.fr.getmymask.helpers.sharedpreferences.SharedPreferencesHelper
import com.crashlytics.android.Crashlytics

import com.app.fr.getmymask.api.ApiClientImpl
import com.app.fr.getmymask.helpers.SharedPref
import com.app.fr.getmymask.token.TokenJWTService

import com.orhanobut.logger.Logger
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import retrofit2.Retrofit
import javax.inject.Inject


class AuthApiServiceImpl @Inject constructor(
    val context: Context,
    retrofit: Retrofit,
    val sharedPreferencesHelper: SharedPreferencesHelper,
val tokenJWTService: TokenJWTService
) : AuthApiService, ApiClientImpl() {

    private var authApi: AuthApi = retrofit.create(AuthApi::class.java)

    override fun register(
        email: String,
        password: String
    ): Single<Int> = Single.create { emitter ->
        authApi.run {
            register(
                email,
                password
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = { response ->
                        if (response.isSuccessful) {
                            emitter.onSuccess(response.code())
                        } else {
                           emitter.onSuccess(response.code())
                        }
                    },
                    onError = { error ->
                        Crashlytics.log(error.localizedMessage)
                        Logger.e(error.localizedMessage)
                        emitter.onError(error)
                    }
                )
        }
    }


    override fun loginUser(email: String, password: String): Single<Boolean> =
        Single.create { emitter ->
            context.doAsync {
                authApi.loginUser(
                    email,
                    password
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                        onNext = { response ->
                            if (response.isSuccessful) {
                                saveTokensUser(response.body()!!.token!!)
                                saveToken(response.body()!!.token!!)
                                emitter.onSuccess(true)
                            } else {
                                if (response.code() == Constants.HTTPStatus.UNAUTHORIZED.code) {
                                    emitter.onSuccess(false)

                                } else {
                                    handleStatusCodeToSendEmitter(
                                        EmitterErrorAdapter(emitter),
                                        response.code()
                                    )
                                }
                            }
                        },
                        onError = {
                            Crashlytics.log(it.localizedMessage)
                            Logger.e(it.localizedMessage)
                            emitter.onError(it)
                        })
            }

        }

    private fun saveTokensUser(token:String): Completable {

        return tokenJWTService.saveTokenIfValid(token)
            .andThen(tokenJWTService.saveRefreshToken(token))
    }

    private fun saveToken(token: String) {
        SharedPref.putString(context, Constants.TOKEN_KEY, token)
    }



}