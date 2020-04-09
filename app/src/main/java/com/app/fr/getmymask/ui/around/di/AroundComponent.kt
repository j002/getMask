package com.app.fr.getmymask.ui.around.di

import com.app.fr.getmymask.di.GlobalInjectorModule
import com.app.fr.getmymask.di.ViewModelModule
import com.app.fr.getmymask.ui.around.AroundActivity
import dagger.Component


@Component(modules = [GlobalInjectorModule::class, ViewModelModule::class])
interface AroundComponent {
    fun inject(aroundActivity: AroundActivity)
}