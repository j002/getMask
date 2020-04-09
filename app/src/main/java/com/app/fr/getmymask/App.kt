package com.app.fr.getmymask

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.lifecycle.ProcessLifecycleOwner
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import com.app.fr.getmymask.di.AppModule
import com.app.fr.getmymask.di.DaggerAppComponent
import com.app.fr.getmymask.helpers.network.ConnectivityReceiver
import com.app.fr.getmymask.token.TokenJWTService
import javax.inject.Inject


class App : Application(), ConnectivityReceiver.ConnectivityReceiverListener, AppLifecycleObserver.AppLifecycleListener,
    ApiErrorListener {

    @Inject
    lateinit var appLifecycleObserver: AppLifecycleObserver

    @Inject
    lateinit var tokenJWTService: TokenJWTService

    private var apiErrorListener: ApiErrorListener? = null

    companion object {
        var isDeviceConnected = false
        var isDeviceBackground = false
    }

    override fun onCreate() {
        super.onCreate()
      //  Logger.addLogAdapter(AndroidLogAdapter())

      //  Fabric.with(this, Crashlytics())

       DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
            .inject(this)


        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
        ConnectivityReceiver.connectivityReceiverListener = this


    }

    fun isLogged(): Boolean {
        return tokenJWTService.hasTokenValid()
    }

    fun getToken(): String {
        return tokenJWTService.getToken()
    }


    fun setInternetConnectionListener(listener: ApiErrorListener?) {
        apiErrorListener = listener
    }

    fun hasInternetConnectionListener(): Boolean {
        return apiErrorListener != null
    }

    fun sendNotif(title: String, message: String, numberDriver: String? = null) {
        if (isDeviceBackground) {
          //  NotificationHelper.sendNotification(this, title, message, numberDriver)
        }
    }

    fun removeApiErrorListner() {
        apiErrorListener = null
    }

    override fun onUnauthorized() {
        apiErrorListener?.onUnauthorized()
    }

    override fun onInternetUnavailable() {
        apiErrorListener?.onInternetUnavailable()
    }

    override fun onInternetAvailable() {
        apiErrorListener?.onInternetAvailable()
    }

    override fun onEnteringForeground() {
        isDeviceBackground = false
        observeConnectivity()
    }

    override fun onEnteringBackGround() {
        isDeviceBackground = true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        isDeviceConnected = isConnected
    }

    private fun observeConnectivity() {
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Habana"
            val description = "Notification Habana"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}

interface ApiErrorListener {
    fun onInternetUnavailable()
    fun onInternetAvailable()
    fun onUnauthorized()
}