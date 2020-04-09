
package com.app.fr.getmymask.di

import android.app.Activity
import android.content.Context
import android.util.Log
import com.app.fr.getmymask.App
import com.app.fr.getmymask.Constants
import com.app.fr.getmymask.helpers.network.NetworkConnectionInterceptor
import com.app.fr.getmymask.helpers.sharedpreferences.SharedPreferencesHelper
import com.app.fr.getmymask.helpers.sharedpreferences.SharedPreferencesHelperImpl
import com.app.fr.getmymask.helpers.variant.VariantHelper
import com.app.fr.getmymask.helpers.variant.VariantHelperImpl
import com.app.fr.getmymask.token.TokenJWTServiceImpl
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.app.fr.getmymask.api.authentification.AuthApiService
import com.app.fr.getmymask.api.mask.MaskApiService
import com.app.fr.getmymask.api.mask.MaskApiServiceImpl
import com.app.fr.getmymask.api.authentification.AuthApiServiceImpl
import com.app.fr.getmymask.helpers.SharedPref
import com.app.fr.getmymask.token.TokenJWTService
import com.squareup.moshi.Moshi
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.OkHttpClient
import org.jetbrains.anko.runOnUiThread
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


@Module
class GlobalInjectorModule(var activity: Activity) {

    @Provides
    fun provideContext(): Context = activity

    @Provides
    fun provideApplication(): App = activity.application as App

    @Provides
    fun provideVariantHelper(helper: VariantHelperImpl): VariantHelper = helper


    // Helper
    @Provides
    fun provideSharedPreferencesHelper(helper: SharedPreferencesHelperImpl): SharedPreferencesHelper = helper


    @Provides
    fun provideTokenJWTService(service: TokenJWTServiceImpl): TokenJWTService = service


    //API
    @Provides
    fun provideAuthApiService(service: AuthApiServiceImpl): AuthApiService = service

    @Provides
    fun provideMaskApiService(service: MaskApiServiceImpl): MaskApiService = service




    @Provides
    fun provideRetrofit(): Retrofit {
        val moshi = Moshi.Builder()
            .build()
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(provideOkHttpClient())
            .baseUrl(provideVariantHelper(VariantHelperImpl()).getBackendEndPoint())
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okhttpClientBuilder = OkHttpClient.Builder()
        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS)
        //todo make better
        okhttpClientBuilder.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + getCredentials(Constants.TOKEN_KEY))
                .build()
            Log.d("BEARER", getCredentials(Constants.TOKEN_KEY))
            var response = chain.proceed(request)
            var tryCount = 0
            if (response.code() == 401 || response.code() == 403) {
                provideContext().runOnUiThread {
                    provideApplication().onUnauthorized()
                }
            } else {
                while (response.code() == 429 && tryCount < 3) {
                    Logger.d("429 : Request is not successful - $tryCount")
                    tryCount++
                    response = chain.proceed(request)
                }
            }

            response
        }


        okhttpClientBuilder.addInterceptor(object : NetworkConnectionInterceptor() {
            override fun isInternetAvailable(): Boolean {
                return App.isDeviceConnected
            }

            override fun onInternetUnavailable() {
                provideContext().runOnUiThread {
                    provideApplication().onInternetUnavailable()
                }
            }
        })

        return okhttpClientBuilder.build()

    }

    private fun getCredentials(key: String): String {
        var token: String? = null
        SharedPref.run {
            getString(activity, key, "").subscribeBy(

                onSuccess = {
                    token = it

                },
                onError = {

                }
            )
        }

        return token.toString()
    }



}