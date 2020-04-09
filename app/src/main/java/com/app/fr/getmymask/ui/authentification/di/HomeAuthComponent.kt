package com.app.fr.getmymask.ui.authentification.di


import com.app.fr.getmymask.di.GlobalInjectorModule
import com.app.fr.getmymask.di.ViewModelModule
import com.app.fr.getmymask.ui.authentification.HomeAuthActivity
import dagger.Component

@Component(modules = [GlobalInjectorModule::class, ViewModelModule::class])
interface HomeAuthComponent {
    fun inject(homeAuthActivity: HomeAuthActivity)
}