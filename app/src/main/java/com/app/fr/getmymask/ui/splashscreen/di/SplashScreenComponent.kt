package com.app.fr.getmymask.ui.splashscreen.di


import com.app.fr.getmymask.di.GlobalInjectorModule
import com.app.fr.getmymask.di.ViewModelModule
import com.app.fr.getmymask.ui.splashscreen.SplashScreenActivity
import dagger.Component


@Component(modules = [GlobalInjectorModule::class, ViewModelModule::class])
interface SplashScreenComponent {
    fun inject(splashScreenActivity: SplashScreenActivity)
}