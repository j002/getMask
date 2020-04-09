package com.app.fr.getmymask.ui.splashscreen

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.fr.getmymask.Constants
import com.app.fr.getmymask.R
import com.app.fr.getmymask.core.BaseActivity
import com.app.fr.getmymask.di.GlobalInjectorModule
import com.app.fr.getmymask.helpers.SharedPref
import com.app.fr.getmymask.ui.around.AroundActivity
import com.app.fr.getmymask.ui.authentification.HomeAuthActivity
import com.app.fr.getmymask.ui.splashscreen.di.DaggerSplashScreenComponent
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_splash_screen.*
import javax.inject.Inject


class SplashScreenActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var splashViewModel: SplashViewModel
    lateinit var  token:String
    private var waitingQuerys = 0


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        DaggerSplashScreenComponent
            .builder()
            .globalInjectorModule(GlobalInjectorModule(this))
            .build()
            .inject(this)

        splashViewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)




       val handler = Handler()


        Thread(Runnable {
            try {
                Thread.sleep(2000)
            } catch (e: Exception) {
            }
            handler.post {
              //  progress_bar.visibility = View.INVISIBLE



                if (getCredentials(Constants.EMAIL_KEY).isNotEmpty()) {
                    openAroundActivity()
                }else{
                    openLoginActivity()
                }


            }
        }).start()
    }

    private fun getCredentials(key: String): String {
        var token: String? = null
        SharedPref.run {
            getString(this@SplashScreenActivity, key, "").subscribeBy(

                onSuccess = {
                    token = it

                },
                onError = {

                }
            )
        }

        return token.toString()
    }




    private fun openLoginActivity(){
        val intent = Intent(applicationContext, HomeAuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openAroundActivity(){
        val intent = Intent(applicationContext, AroundActivity::class.java)
        startActivity(intent)
        finish()
    }


}
