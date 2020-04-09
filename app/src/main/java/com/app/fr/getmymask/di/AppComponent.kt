
package com.app.fr.getmymask.di

import com.app.fr.getmymask.App
import dagger.Component


@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun inject(application: App)
}