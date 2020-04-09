
package com.app.fr.getmymask.di

import android.content.Context
import com.app.fr.getmymask.App
import com.app.fr.getmymask.AppLifecycleObserver
import com.app.fr.getmymask.helpers.sharedpreferences.SharedPreferencesHelper
import com.app.fr.getmymask.helpers.sharedpreferences.SharedPreferencesHelperImpl
import com.app.fr.getmymask.token.TokenJWTServiceImpl

import com.app.fr.getmymask.token.TokenJWTService
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: App) {

    @Provides
    fun provideApplicationContext(): Context = application

    @Provides
    fun provideAppLifecycleObserver(): AppLifecycleObserver = AppLifecycleObserver(application)

    @Provides
    fun provideSharedPreferencesHelper(): SharedPreferencesHelper = SharedPreferencesHelperImpl()

    @Provides
    fun provideTokenJWTService(service: TokenJWTServiceImpl): TokenJWTService = service

}