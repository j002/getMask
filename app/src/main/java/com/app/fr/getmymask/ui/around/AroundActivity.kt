package com.app.fr.getmymask.ui.around

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.fr.getmymask.R
import com.app.fr.getmymask.core.BaseActivity
import com.app.fr.getmymask.di.GlobalInjectorModule
import com.app.fr.getmymask.ui.around.di.DaggerAroundComponent
import javax.inject.Inject

class AroundActivity : BaseActivity() {

    lateinit var aroundViewModel: AroundViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_around)
        DaggerAroundComponent
            .builder()
            .globalInjectorModule(GlobalInjectorModule(this))
            .build()
            .inject(this)

        aroundViewModel =  ViewModelProviders.of(this, viewModelFactory).get(AroundViewModel::class.java)

    }
}
