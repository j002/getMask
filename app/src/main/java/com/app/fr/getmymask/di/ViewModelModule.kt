
package com.app.fr.getmymask.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.fr.getmymask.ui.around.AroundViewModel
import com.app.fr.getmymask.ui.authentification.HomeAuthViewModel
import com.app.fr.getmymask.ui.splashscreen.SplashViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeAuthViewModel::class)
    internal abstract fun homeAuthViewModel(homeAuthViewModel: HomeAuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AroundViewModel::class)
    internal abstract fun aroundViewModel(aroundViewModel: AroundViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun splashViewModel(splashViewModel: SplashViewModel): ViewModel


}
