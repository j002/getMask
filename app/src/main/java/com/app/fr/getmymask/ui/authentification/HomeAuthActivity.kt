package com.app.fr.getmymask.ui.authentification

import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.fr.getmymask.R
import com.app.fr.getmymask.core.BaseActivity
import com.app.fr.getmymask.di.GlobalInjectorModule
import com.app.fr.getmymask.ui.authentification.di.DaggerHomeAuthComponent

import javax.inject.Inject

class HomeAuthActivity : BaseActivity() {

    private lateinit var homeAuthViewModel: HomeAuthViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_auth)

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        DaggerHomeAuthComponent
            .builder()
            .globalInjectorModule(GlobalInjectorModule(this))
            .build()
            .inject(this)
        homeAuthViewModel =  ViewModelProviders.of(this, viewModelFactory).get(HomeAuthViewModel::class.java)


    }
}
