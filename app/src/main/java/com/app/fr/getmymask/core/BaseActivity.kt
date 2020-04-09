package com.app.fr.getmymask.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andrognito.flashbar.Flashbar
import com.app.fr.getmymask.R
import com.app.fr.getmymask.ApiErrorListener
import com.app.fr.getmymask.App
import com.leclub.app.helpers.FlashbarHelper
import com.app.fr.getmymask.ui.dialogs.ProgressDialogFragment
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.rxkotlin.subscribeBy
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

abstract class BaseActivity : AppCompatActivity(), ApiErrorListener {


   private lateinit var progressDialog: ProgressDialogFragment
    private var refreshPageIfNetworkBack: Boolean = false
    private lateinit var flashBarNoNetwork: Flashbar
    lateinit var app: App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as App
        app.setInternetConnectionListener(this)
        initializeRxJavaErrorHandler()
        createProgressDialog()
        flashBarNoNetwork = FlashbarHelper.createFlashBarError(
            this,
            getString(R.string.flashbar_becarful_no_network),
            getString(R.string.flashbar_message_no_network)
        )
    }

    override fun onResume() {
        super.onResume()
        if (!App.isDeviceConnected) {
            showFlashBarWithDelay()
        }
        if (!(application as App).hasInternetConnectionListener()) {
            (application as App).setInternetConnectionListener(this)
        }
        if (RxJavaPlugins.getErrorHandler() == null) {
            initializeRxJavaErrorHandler()
        }
        if (refreshPageIfNetworkBack) {
            if (App.isDeviceConnected) {
                finish()
                startActivity(intent)
            } else {
                //lostConnectivityDialog.show(this, "lostConnectivityDialog")
            }
            refreshPageIfNetworkBack = false
        }
    }

    override fun onPause() {
        super.onPause()
        if (!App.isDeviceConnected) {
            refreshPageIfNetworkBack = true
        }
        (application as App).setInternetConnectionListener(null)
        RxJavaPlugins.setErrorHandler(null)
    }



    override fun onInternetAvailable() {
        if (flashBarNoNetwork.isShown()) {
            flashBarNoNetwork.dismiss()
        }
    }

    override fun onInternetUnavailable() {
        showFlashBarWithDelay()
        toast("no network")
    }

    override fun onUnauthorized() {
        /*
        if (this !is LoginActivity && this !is RegisterActivity) {

            HomeAuthActivity.startActivity(this)
            toast("unauthorized")
        }
        */
    }

    private fun createProgressDialog() {
        progressDialog = ProgressDialogFragment.createDialog()
    }



    private fun initializeRxJavaErrorHandler() {

        RxJavaPlugins.setErrorHandler { e ->
            var throwable = e

            if (throwable is OnErrorNotImplementedException) {
                throwable = throwable.cause
            }
            if (throwable.localizedMessage == null) {
                Logger.e(throwable.getStackTraceString())
            } else {
                Logger.e(throwable.getStackTraceString())
            }

            Observable.timer(250, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    FlashbarHelper
                        .createFlashBarError(
                            this,
                            getString(R.string.flashbar_becarful_error),
                            getString(R.string.flashbar_message_error)
                        )
                        .show()
                }

            //  Crashlytics.logException(throwable)

        }
    }

    fun showProgressDialog() {
        supportFragmentManager.executePendingTransactions()
        if (!progressDialog.isAdded) {
            progressDialog.show(this, "ProgressDialog")
        }
    }

    fun hideProgressDialog() {
        if (progressDialog.isAdded) {
            progressDialog.dismiss()
        }
    }



    fun isLogged(): Boolean {
        return app.isLogged()
    }

    fun showErrorFlashBar(message: String) {
        FlashbarHelper
            .createFlashBarError(this, getString(R.string.flashbar_becarful_no_network), message)
            .show()
    }

    private fun showFlashBarWithDelay() {
      /* if (!flashBarNoNetwork.isShown() && this !is SplashScreenActivity) {
            Handler().postDelayed({
                flashBarNoNetwork.show()
            }, 250)
        }*/
    }
}